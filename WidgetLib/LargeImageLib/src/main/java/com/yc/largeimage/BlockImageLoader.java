
package com.yc.largeimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Build;
import androidx.core.util.Pools;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;

import com.yc.largeimage.factory.BitmapDecoderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class BlockImageLoader {

    private static int BASE_BLOCKSIZE;

    private Context context;

    public static boolean DEBUG = false;
    static final String TAG = "Loader";
    private static Pools.SynchronizedPool<Bitmap> bitmapPool = new Pools.SynchronizedPool<>(6);
    private Pools.SimplePool<BlockData> blockDataPool = new Pools.SimplePool<>(64);
    private Pools.SimplePool<DrawData> drawDataPool = new Pools.SimplePool<>(64);
    private LoadData mLoadData;
    private OnImageLoadListener onImageLoadListener;
    private TaskQueue taskQueue;
    private OnLoadStateChangeListener onLoadStateChangeListener;

    public BlockImageLoader(Context context) {
        super();
        this.taskQueue = new TaskQueue();
        this.context = context;
        if (BASE_BLOCKSIZE <= 0) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            BASE_BLOCKSIZE = (metrics.heightPixels + metrics.widthPixels) / 4 + (((metrics.heightPixels + metrics.widthPixels) % 4) == 0 ? 2 : 1);
        }
    }

    public boolean hasLoad() {
        LoadData loadData = mLoadData;
        return loadData != null && loadData.mDecoder != null;
    }

    public void setOnImageLoadListener(OnImageLoadListener onImageLoadListener) {
        this.onImageLoadListener = onImageLoadListener;
    }

    public void setOnLoadStateChangeListener(OnLoadStateChangeListener onLoadStateChangeListener) {
        this.onLoadStateChangeListener = onLoadStateChangeListener;
    }

    /**
     * 设置图片的DecoderFactory
     *
     * @param factory BitmapRegionDecoder加载的工厂
     */
    public void setBitmapDecoderFactory(BitmapDecoderFactory factory) {
        if (mLoadData != null) {
            release(mLoadData);
        }
        this.mLoadData = new LoadData(factory);
    }

    private void release(LoadData loadData) {
        if (DEBUG) {
            Log.d(TAG, "release loadData:" + loadData);
        }
        cancelTask(loadData.task);
        loadData.task = null;
        recycleMap(loadData.smallDataMap);
        recycleMap(loadData.currentScaleDataMap);
    }

    public void stopLoad() {
        if (mLoadData != null) {
            if (DEBUG) {
                Log.d(TAG, "stopLoad ");
            }
            cancelTask(mLoadData.task);
            mLoadData.task = null;
            Map<Position, BlockData> currentScaleDataMap = mLoadData.currentScaleDataMap;
            if (currentScaleDataMap != null) {
                for (BlockData blockData : currentScaleDataMap.values()) {
                    cancelTask(blockData.task);
                    blockData.task = null;
                }
            }
        }
    }

    public void loadImageBlocks(List<DrawData> drawDataList, float imageScale, Rect showImageRect, int viewWidth, int viewHeight) {
        LoadData loadData = mLoadData;
        //根据外部传进来的缩放尺寸转化成2的指数次方的值
        //因为手势放大缩小操作要加载不同清晰度的图片区域，比如之前的图片缩放是4，现在缩放是4.2，难道要重新加载？
        //通过public int getNearScale(float imageScale)方法计算趋于2的指数次方的值（1，2，4，8，16）
        // 比如3.9和4.2和4比较接近，就直接加载图片显示比例为4的图片块
        int scale = getNearScale(imageScale);
        int preScale = loadData.currentScale;

        for (DrawData drawData : drawDataList) {
            drawData.bitmap = null;
            drawDataPool.release(drawData);
        }
        drawDataList.clear();

        if (loadData.mDecoder == null) {
            if (isUnRunning(loadData.task)) {
                loadData.task = new LoadImageInfoTask(loadData, onImageLoadListener, onLoadStateChangeListener);
                exeTask(loadData.task);
            }
            return;
        }
        int imageWidth = loadData.imageWidth;
        int imageHeight = loadData.imageHeight;
        BitmapRegionDecoder decoder = loadData.mDecoder;
        if (loadData.thumbnailBlockData == null) {
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            int s = (int) Math.ceil(Math.sqrt(1.0 * (imageWidth * imageHeight) / ((screenWidth / 2) * (screenHeight / 2))));
            int thumbnailScale = getNearScale(s);
            if (thumbnailScale < s) {
                thumbnailScale *= 2;
            }
            loadData.thumbnailScale = thumbnailScale;
            loadData.thumbnailBlockData = new BlockData();
        }
        int thumbnailScale = loadData.thumbnailScale;
        if (loadData.thumbnailBlockData.bitmap == null) {
            if (isUnRunning(loadData.thumbnailBlockData.task)) {
                loadData.thumbnailBlockData.task = new LoadThumbnailTask(loadData, decoder, thumbnailScale, imageWidth, imageHeight, onImageLoadListener, onLoadStateChangeListener);
                exeTask(loadData.thumbnailBlockData.task);
            }
        } else {
            DrawData drawData = drawDataPool.acquire();
            if (drawData == null) {
                drawData = new DrawData();
            }
            //如果有缩略图，只绘制缩略图的显示区域
            drawData.imageRect.set(showImageRect);
            int cache = dip2px(context, 100);
            cache = (int) (cache * imageScale);
            drawData.imageRect.right += cache;
            drawData.imageRect.top -= cache;
            drawData.imageRect.left -= cache;
            drawData.imageRect.bottom += cache;

            if (drawData.imageRect.left < 0) {
                drawData.imageRect.left = 0;
            }
            if (drawData.imageRect.top < 0) {
                drawData.imageRect.top = 0;
            }
            if (drawData.imageRect.right > imageWidth) {
                drawData.imageRect.right = imageWidth;
            }
            if (drawData.imageRect.bottom > imageHeight) {
                drawData.imageRect.bottom = imageHeight;
            }

            drawData.srcRect.left = (int) Math.abs(1.0f * drawData.imageRect.left / thumbnailScale);
            drawData.srcRect.right = (int) Math.abs(1.0f * drawData.imageRect.right / thumbnailScale);
            drawData.srcRect.top = (int) Math.abs(1.0f * drawData.imageRect.top / thumbnailScale);
            drawData.srcRect.bottom = (int) Math.abs(1.0f * drawData.imageRect.bottom / thumbnailScale);
            drawData.bitmap = loadData.thumbnailBlockData.bitmap;

            drawDataList.add(drawData);
        }

        if (DEBUG)
            Log.d(TAG, "loadImageBlocks ---------- imageRect:" + showImageRect + " imageScale:" + imageScale + " currentScale:" + scale);

        //如果缩略图的清晰够用，就不需要去加载图片块，直接画缩略图就好啦
        if (thumbnailScale <= scale) {
            return;
        }

        // 横向分多少份，纵向分多少分，才合理？加载的图片块宽高像素多少？
//        我采用了基准块（图片比例是1，一个图片块的宽高的sise） BASEBLOCKSIZE = context.getResources().getDisplayMetrics().widthPixels / 2;
//        图片缩放比例为1的话，图片块宽高是BASEBLOCKSIZE
//        图片缩放比例为4的话，图片块宽高是4*BASEBLOCKSIZE
//        图片没被位移，那么屏幕上显示横向2列，纵向getDisplayMetrics().heightPixels/BASEBLOCKSIZE行

        int blockSize = BASE_BLOCKSIZE * scale;
        int realBlockSize = (BASE_BLOCKSIZE * scale);

        //计算完整图片被切分图片块的最大行数，和最大列数
        int maxRow = imageHeight / realBlockSize + (imageHeight % realBlockSize == 0 ? 0 : 1);
        int maxCol = imageWidth / realBlockSize + (imageWidth % realBlockSize == 0 ? 0 : 1);

        // 该scale下对应的position范围
        int startRow = showImageRect.top / realBlockSize;
        int endRow = showImageRect.bottom / realBlockSize + (showImageRect.bottom % realBlockSize == 0 ? 0 : 1);
        int startCol = showImageRect.left / realBlockSize;
        int endCol = showImageRect.right / realBlockSize + (showImageRect.right % realBlockSize == 0 ? 0 : 1);
        if (startRow < 0) {
            startRow = 0;
        }
        if (startCol < 0) {
            startCol = 0;
        }
        if (endRow > maxRow) {
            endRow = maxRow;
        }
        if (endCol > maxCol) {
            endCol = maxCol;
        }

        int cacheStartRow = startRow;
        int cacheEndRow = endRow;
        int cacheStartCol = startCol;
        int cacheEndCol = endCol;

        if ((endCol - startCol) * (endRow - startRow) <= 16) {
            int topEdgeShowImageSize = realBlockSize - showImageRect.top % realBlockSize;
            int bottomEdgeShowImageSize = (showImageRect.bottom % realBlockSize);
            int rowEdgePreLoadSize;
            if (endCol - startCol == 1) {//显示1列的预加载策略
                rowEdgePreLoadSize = 0; //多加载一行
            } else if (endCol - startCol == 2) {//显示两列的预加载策略
                rowEdgePreLoadSize = realBlockSize / 2;     //图片块显示的部分超过4分之3就多加载一行
            } else if (endCol - startCol == 3) {//显示3列的预加载策略
                rowEdgePreLoadSize = realBlockSize / 8 * 7;   //图片块显示的部分超过16分之15就多加载一行
            } else {
                rowEdgePreLoadSize = Integer.MAX_VALUE;  //不预加载行
            }
            if (topEdgeShowImageSize > bottomEdgeShowImageSize) {
                if (topEdgeShowImageSize > rowEdgePreLoadSize) {
                    cacheStartRow += 1;
                }
            } else {
                if (bottomEdgeShowImageSize > rowEdgePreLoadSize) {
                    cacheEndRow += 1;
                }
            }

            int leftEdgeShowImageSize = realBlockSize - showImageRect.left % realBlockSize;
            int rightEdgeShowImageSize = (showImageRect.right % realBlockSize);
            int colEdgePreLoadSize;
            if (endRow - startRow == 1) {
                colEdgePreLoadSize = 0;
            } else if (endRow - startRow == 2) {
                colEdgePreLoadSize = realBlockSize / 2;
            } else if (endRow - startRow == 3) {
                colEdgePreLoadSize = realBlockSize / 8 * 7;
            } else {
                colEdgePreLoadSize = Integer.MAX_VALUE;
            }
            if (leftEdgeShowImageSize > rightEdgeShowImageSize) {
                if (leftEdgeShowImageSize > colEdgePreLoadSize) {
                    cacheStartCol += 1;
                }
            } else {
                if (rightEdgeShowImageSize > colEdgePreLoadSize) {
                    cacheStartCol += 1;
                }
            }

            if (cacheStartRow < 0) {
                cacheStartRow = 0;
            }
            if (cacheStartCol < 0) {
                cacheStartCol = 0;
            }
            if (cacheEndRow > maxRow) {
                cacheEndRow = maxRow;
            }
            if (cacheEndCol > maxCol) {
                cacheEndCol = maxCol;
            }
        } else {
            cacheEndRow = endRow;
            cacheStartRow = startRow;
            cacheEndCol = endCol;
            cacheStartCol = startCol;
        }

        //需要加载的图片块的坐标，Position其实就是[row,col]
        List<Position> needShowPositions = new LinkedList<>();
        /**
         * decodingOptions.inSampleSize = currentScale 假设smallDataMap的scale = 4，那么currentScaleDataMap = 2, largeDataMap 对应为 1
         * currentScale 为4时图片解码的宽高缩放了4倍  -> （相对模糊）
         * currentScale 为2时图片解码的宽高缩放了2倍
         * currentScale 为1时表示按原图比例显示，     -> （相对清晰）
         * 所以largeDataMap的单位BlockData占图片比例的区域最小
         * 还有一个特性是： 图片块（非边缘的图片块），small,current,large的bitmap的width以及height是一样的
         *
         *    手势缩小需要用到的缓存map，BlockData的bitmap显示占图片比例的区域大，（图片块相对模糊）1个图片块显示4个currentScale图片块的内容
         * Map<Position, BlockData> smallDataMap;
         *
         *    当前用到的map  , 1个图片块显示4个large图片块的内容
         * <Position, BlockData> currentScaleDataMap;
         */
        //如果当前的图片缩放切块列表不为空并且不是需要的缩放级别
        //scale变小，表示放大
        if (loadData.currentScaleDataMap != null && preScale != scale) {
            Map<Position, BlockData> preSmall = loadData.smallDataMap;
            Map<Position, BlockData> preCurrent = loadData.currentScaleDataMap;
            if (DEBUG) {
                Log.d(TAG, "preScale:" + preScale + " currentScale:" + scale + " ds:" + (1.0f * scale / preScale));
            }
            if (scale == preScale * 2) {//相当于图片通过手势缩小了2倍，原先相对模糊的small 已经被定义为 当前的缩放度
                loadData.currentScaleDataMap = preSmall;
                //回收
                recycleMap(preCurrent);
                loadData.smallDataMap = preCurrent;
                if (DEBUG) {
                    Log.d(TAG, "相当于图片通过手势缩小了2倍，原先相对模糊的small 已经被定义为 当前的缩放度");
                }
            } else if (scale == preScale / 2) {//相当于通过手势放大了2倍，原先相对清晰的large 已经被定义为 当前的缩放度
                loadData.smallDataMap = preCurrent;
                //回收
                recycleMap(preSmall);
                loadData.currentScaleDataMap = preSmall;
                if (DEBUG) {
                    Log.d(TAG, "相当于通过手势放大了2倍，原先相对清晰的large 已经被定义为 当前的缩放度");
                }
            } else { //相对原先 缩小倍数过多，放大倍数过多，这种情况是直接设置scale，通过手势都会走上面的倍数
                //回收
                recycleMap(preSmall);
                recycleMap(preCurrent);
                if (DEBUG) {
                    Log.d(TAG, "相对原先 缩小倍数过多，放大倍数过多，这种情况是直接设置scale，通过手势都会走上面的倍数");
                }
            }
        }
        loadData.currentScale = scale;

        if (loadData.currentScaleDataMap == null) {
            loadData.currentScaleDataMap = new HashMap<>();
        }
        /*
        * 找出该scale所有存在的图片切块，和记录所有不存在的position
		*/
        Position positionKey = new Position();
        List<DrawData> currentDrawCaches = new ArrayList<>();
        Map<Position, BlockData> tempCurrentDataMap = loadData.currentScaleDataMap;
        loadData.currentScaleDataMap = new HashMap<>();
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                positionKey.set(row, col);
                //blockData可能为null
                BlockData blockData = addRequestBlock(positionKey, tempCurrentDataMap.get(positionKey), loadData.currentScaleDataMap, scale, imageWidth, imageHeight, decoder);
                Bitmap bitmap = blockData.bitmap;
                if (bitmap != null) {
                    DrawData drawData = drawDataPool.acquire();
                    if (drawData == null) {
                        drawData = new DrawData();
                    }
                    Rect rect = drawData.imageRect;
                    rect.left = (col * blockSize);
                    rect.top = (row * blockSize);
                    rect.right = rect.left + blockData.realImageRect.width() * scale;
                    rect.bottom = rect.top + blockData.realImageRect.height() * scale;
                    drawData.srcRect.set(0, 0, blockData.realImageRect.width(), blockData.realImageRect.height());
                    drawData.bitmap = blockData.bitmap;
                    currentDrawCaches.add(drawData);
                    if (DEBUG)
                        Log.d(TAG, "cache add--  添加  normal position :" + positionKey + " src:" + drawData.srcRect + " imageRect:" + drawData.imageRect + " w:" + drawData.imageRect.width() + " h:" + drawData.imageRect.height());
                } else {
                    needShowPositions.add(new Position(row, col));
                }
            }
        }
        /**
         *  预先加载的缓存区域，不在屏幕上显示的区域
         * <pre>
         * #########
         * #       #
         * #       #
         * #########
         * <pre>
         */
        // 上 #########
        for (int row = cacheStartRow; row < startRow; row++) {
            for (int col = cacheStartCol; col < cacheEndCol; col++) {
                positionKey.set(row, col);
                addRequestBlock(positionKey, tempCurrentDataMap.get(positionKey), loadData.currentScaleDataMap, scale, imageWidth, imageHeight, decoder);
            }
        }
        // 下 #########
        for (int row = endRow; row < cacheEndRow; row++) {
            for (int col = cacheStartCol; col < cacheEndCol; col++) {
                positionKey.set(row, col);
                addRequestBlock(positionKey, tempCurrentDataMap.get(positionKey), loadData.currentScaleDataMap, scale, imageWidth, imageHeight, decoder);
            }
        }
        // # 左
        // #
        for (int row = startRow; row < endRow; row++) {
            for (int col = cacheStartCol; col < startCol; col++) {
                positionKey.set(row, col);
                addRequestBlock(positionKey, tempCurrentDataMap.get(positionKey), loadData.currentScaleDataMap, scale, imageWidth, imageHeight, decoder);
            }
        }
        // # 右
        // #
        for (int row = startRow; row < endRow; row++) {
            for (int col = endCol; col < cacheEndCol; col++) {
                positionKey.set(row, col);
                addRequestBlock(positionKey, tempCurrentDataMap.get(positionKey), loadData.currentScaleDataMap, scale, imageWidth, imageHeight, decoder);
            }
        }
        //移除掉那些没被用到的缓存的图片块
        tempCurrentDataMap.keySet().removeAll(loadData.currentScaleDataMap.keySet());
        if (DEBUG) {
            Log.d(TAG, "preCurrentDataMap: " + tempCurrentDataMap.toString() + " needShowPositions：" + needShowPositions);
        }
        recycleMap(tempCurrentDataMap);

        List<DrawData> smallDataList = loadSmallDatas(loadData, scale, needShowPositions, startRow, endRow, startCol, endCol);
        drawDataList.addAll(smallDataList);
        drawDataList.addAll(currentDrawCaches);
        if (DEBUG) {
            Log.d(TAG, "detail current scale:" + scale + " startRow:" + startRow + " endRow:" + endRow + " startCol:" + startCol + " endCol:" + endCol + " blockSize:" + blockSize + " size:" + loadData.currentScaleDataMap.size() + " small size:" + (loadData.smallDataMap == null ? "null" : loadData.smallDataMap.size()));
            Log.d(TAG, "detail thumbnailScale:" + thumbnailScale + " cacheStartRow:" + cacheStartRow + " cacheEndRow:" + cacheEndRow + " cacheStartCol:" + cacheStartCol + " cacheEndCol:" + cacheEndCol + " draDataList.size:" + drawDataList.size());
            Log.d(TAG, "detail imageRect:" + showImageRect);
            sparseIntArray.put(loadData.currentScaleDataMap.size(), sparseIntArray.get(loadData.currentScaleDataMap.size(), 0) + 1);
            StringBuilder s = new StringBuilder();
            s.append("detail 统计次数 ");
            for (int i = 0; i < sparseIntArray.size(); i++) {
                s.append("size:" + sparseIntArray.keyAt(i) + "->time:" + sparseIntArray.valueAt(i) + "  ");
            }
            Log.d(TAG, s.toString());
        }
//        if(blockData1.size()>0)

    }

    private SparseIntArray sparseIntArray = new SparseIntArray();

    private List<DrawData> loadSmallDatas(LoadData loadData, int scale, List<Position> needShowPositions, int startRow, int endRow, int startCol, int endCol) {
        List<DrawData> cacheDraws = new ArrayList<>();
        if (DEBUG) {
            Log.d(TAG, "之前 loadData.largeDataMap :" + (loadData.smallDataMap == null ? "null" : loadData.smallDataMap.size()));
        }
        Position currentScalePosition = new Position();
        //largeDataMap: 手势放大后需要用到的缓存map，BlockData的bitmap显示占图片比例的区域小，（图片块相对清晰）
        if (loadData.smallDataMap != null && !loadData.smallDataMap.isEmpty()) {
            int scaleKey = scale * 2;
            int ds = scaleKey / scale;
            // 单位图片的真实区域
            int size = scale * BASE_BLOCKSIZE;

            // 显示区域范围
            int startRowKey = startRow / 2;
            int endRowKey = endRow / 2;
            int startColKey = startCol / 2;
            int endColKey = endCol / 2;

            Iterator<Map.Entry<Position, BlockData>> blockIterator = loadData.smallDataMap.entrySet().iterator();
            while (blockIterator.hasNext()) {
                Map.Entry<Position, BlockData> entry = blockIterator.next();
                Position position = entry.getKey();
                BlockData blockData = entry.getValue();
                if (DEBUG) {
                    Log.d(TAG, "cache add-- 遍历 largeDataMap position :" + position);
                }
                //取消加载图片的任务
                cancelTask(blockData.task);
                loadData.task = null;
                if (needShowPositions.isEmpty()) {
                    continue;
                }
                if (blockData.bitmap != null && position.row >= startRowKey && position.row <= endRowKey && position.col >= startColKey && position.col <= endColKey) {
                    int startPositionRow = position.row * ds;
                    int endPositionRow = startPositionRow + ds;
                    int startPositionCol = position.col * ds;
                    int endPositionCol = startPositionCol + ds;

                    int iW = blockData.realImageRect.width();
                    int iH = blockData.realImageRect.height();

                    // 单位图片的大小
                    int blockImageSize = (int) Math.ceil(1.0f * BASE_BLOCKSIZE / ds);

                    for (int row = startPositionRow, i = 0; row < endPositionRow; row++, i++) {
                        int top = i * blockImageSize;
                        if (top >= iH) {
                            break;
                        }
                        for (int col = startPositionCol, j = 0; col < endPositionCol; col++, j++) {
                            int left = j * blockImageSize;
                            if (left >= iW) {
                                break;
                            }
                            if (needShowPositions.remove(currentScalePosition.set(row, col))) {
                                int right = left + blockImageSize;
                                int bottom = top + blockImageSize;
                                if (right > iW) {
                                    right = iW;
                                }
                                if (bottom > iH) {
                                    bottom = iH;
                                }
                                DrawData drawData = drawDataPool.acquire();
                                if (drawData == null) {
                                    drawData = new DrawData();
                                }
                                drawData.bitmap = blockData.bitmap;
                                Rect rect = drawData.imageRect;
                                rect.left = col * size;
                                rect.top = row * size;
                                rect.right = rect.left + (right - left) * scaleKey;
                                rect.bottom = rect.top + (bottom - top) * scaleKey;
                                drawData.srcRect.set(left, top, right, bottom);
                                drawData.bitmap = blockData.bitmap;
                                cacheDraws.add(drawData);
                                if (DEBUG) {
                                    Log.d(TAG, "cache add--添加  smallDataMap position :" + position + " 到 当前currentScalePosition:" + currentScalePosition + " src:" + drawData.srcRect + "w:" + drawData.srcRect.width() + " h:" + drawData.srcRect.height() + " imageRect:" + drawData.imageRect + " w:" + drawData.imageRect.width() + " h:" + drawData.imageRect.height());
                                }
                            }
                        }
                    }
                } else {
                    blockIterator.remove();
                    recycleBlock(blockData);
                }
            }
        }
        return cacheDraws;
    }

    private boolean isUnRunning(TaskQueue.Task task) {
        return task == null;
    }

    private void exeTask(TaskQueue.Task task) {
        taskQueue.addTask(task);
    }

    private void cancelTask(TaskQueue.Task task) {
        if (task != null) {
            taskQueue.cancelTask(task);
        }
    }

    private void recycleMap(Map<Position, BlockData> map) {
        if (map == null) {
            return;
        }
        for (Map.Entry<Position, BlockData> entry : map.entrySet()) {
            recycleBlock(entry.getValue());
        }
        map.clear();
    }

    private void recycleBlock(BlockData block) {
        cancelTask(block.task);
        block.task = null;
        if (block.bitmap != null) {
            bitmapPool.release(block.bitmap);
            block.bitmap = null;
        }
        blockDataPool.release(block);
    }

    private BlockData addRequestBlock(Position positionKey, BlockData blockData, Map<Position, BlockData> currentDataMap, int scale, int imageWidth, int imageHeight, BitmapRegionDecoder decoder) {
        if (blockData == null) {
            blockData = blockDataPool.acquire();
            if (blockData == null) {
                blockData = new BlockData(new Position(positionKey.row, positionKey.col));
            } else {
                if (blockData.position == null) {
                    blockData.position = new Position(positionKey.row, positionKey.col);
                } else {
                    blockData.position.set(positionKey.row, positionKey.col);
                }
            }
        }
        if (blockData.bitmap == null && isUnRunning(blockData.task)) {
            blockData.task = new LoadBlockTask(blockData.position, blockData, scale, imageWidth, imageHeight, decoder, onImageLoadListener, onLoadStateChangeListener);
            exeTask(blockData.task);
        }
        currentDataMap.put(blockData.position, blockData);
        return blockData;
    }


    /**
     * 1,2,3,4,5,6,7,8,9
     * 1,2,4,4,8,8,8,8,16
     * 获取2的乘方的数 , 1，2，4，8这种数字<br>
     *
     * @param imageScale 缩放比例
     * @return 转化为2的次方数
     */
    private int getNearScale(float imageScale) {
        int scale = Math.round(imageScale);
        return getNearScale(scale);
    }

    private int getNearScale(int scale) {
        int result = 1;
        while (result < scale) {
            result *= 2;
        }
        return result;
    }

    static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    int getWidth() {
        if (mLoadData == null) {
            return 0;
        }
        return mLoadData.imageWidth;
    }

    int getHeight() {
        if (mLoadData == null) {
            return 0;
        }
        return mLoadData.imageHeight;
    }

    private static class LoadData {
        int currentScale;
        /**
         * decodingOptions.inSampleSize = currentScale 假设smallDataMap的scale = 2，那么currentScaleDataMap = 1
         * currentScale 为2时图片解码的宽高缩放了2倍  -> （相对模糊）
         * currentScale 为1时图片为原图
         * 还有一个特性是： 图片块（非边缘的图片块），small,current的bitmap的width以及height是一样的
         */
        //手势缩小后需要用到的缓存map，BlockData的bitmap显示占图片比例的区域大，（图片块相对模糊）1个图片块显示4个currentScale图片块的内容
        Map<Position, BlockData> smallDataMap;
        //当前用到的map
        Map<Position, BlockData> currentScaleDataMap;

        /**
         * 完整图片的缩略图，用于一开始展示，避免一开始没有加载好图片块，导致空白
         */
        private volatile BlockData thumbnailBlockData;
        /**
         * 完整图片的缩略图
         */
        private volatile int thumbnailScale;

        private BitmapDecoderFactory mFactory;
        private BitmapRegionDecoder mDecoder;
        private int imageHeight;
        private int imageWidth;
        private LoadImageInfoTask task;

        LoadData(BitmapDecoderFactory factory) {
            mFactory = factory;
        }
    }

    private static class BlockData {
        BlockData() {
        }

        BlockData(Position position) {
            this.position = position;
        }

        Bitmap bitmap;

        //图片的区域
        Rect realImageRect = new Rect();

        TaskQueue.Task task;
        Position position;
    }

    static class DrawData {
        //绘制到View上的区域
        Rect srcRect = new Rect();
        //图片的区域
        Rect imageRect = new Rect();
        Bitmap bitmap;
    }

    /**
     * 图片块的位置
     */
    private static class Position {
        int row;
        int col;

        Position() {
            super();
        }

        Position(int row, int col) {
            super();
            this.row = row;
            this.col = col;
        }

        Position set(int row, int col) {
            this.row = row;
            this.col = col;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Position) {
                Position position = (Position) o;
                return row == position.row && col == position.col;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int iTotal = 17;
            int iConstant = 37;
            iTotal = iTotal * iConstant + row;
            iTotal = iTotal * iConstant + col;
            return iTotal;
        }

        @Override
        public String toString() {
            return "row:" + row + " col:" + col;
        }
    }

    private static class LoadImageInfoTask extends TaskQueue.Task {
        private BitmapDecoderFactory mFactory;
        private LoadData imageInfo;
        private OnLoadStateChangeListener onLoadStateChangeListener;
        private OnImageLoadListener onImageLoadListener;
        private volatile BitmapRegionDecoder decoder;
        private volatile int imageWidth;
        private volatile int imageHeight;
        private volatile Exception e;

        LoadImageInfoTask(LoadData loadData, OnImageLoadListener onImageLoadListener, OnLoadStateChangeListener onLoadStateChangeListener) {
            this.imageInfo = loadData;
            mFactory = imageInfo.mFactory;
            this.onImageLoadListener = onImageLoadListener;
            this.onLoadStateChangeListener = onLoadStateChangeListener;
            if (DEBUG) {
                Log.d(TAG, "start LoadImageInfoTask:imageW:" + imageWidth + " imageH:" + imageHeight);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (onLoadStateChangeListener != null) {
                onLoadStateChangeListener.onLoadStart(LOAD_TYPE_INFO, null);
            }
        }

        @Override
        protected void doInBackground() {
            try {
                decoder = mFactory.made();
                imageWidth = decoder.getWidth();
                imageHeight = decoder.getHeight();
                if (DEBUG) {
                    Log.d(TAG, "LoadImageInfoTask doInBackground");
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.e = e;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            onLoadStateChangeListener = null;
            onImageLoadListener = null;
            mFactory = null;
            imageInfo = null;
            if (DEBUG) {
                Log.d(TAG, "LoadImageInfoTask: onCancelled");
            }
        }

        @Override
        protected void onPostExecute() {
            super.onPostExecute();
            if (DEBUG) {
                Log.d(TAG, "onPostExecute LoadImageInfoTask:" + e + " imageW:" + imageWidth + " imageH:" + imageHeight + " e:" + e);
            }
            imageInfo.task = null;
            if (e == null) {
                imageInfo.imageWidth = imageWidth;
                imageInfo.imageHeight = imageHeight;
                imageInfo.mDecoder = decoder;
                onImageLoadListener.onLoadImageSize(imageWidth, imageHeight);
            } else {
                onImageLoadListener.onLoadFail(e);
            }
            if (onLoadStateChangeListener != null) {
                onLoadStateChangeListener.onLoadFinished(LOAD_TYPE_INFO, null, e == null, e);
            }
            onLoadStateChangeListener = null;
            onImageLoadListener = null;
            mFactory = null;
            imageInfo = null;
        }
    }

    private static class LoadBlockTask extends TaskQueue.Task {
        private int scale;
        private BlockData blockData;
        private Position position;
        private int imageWidth;
        private int imageHeight;
        private BitmapRegionDecoder decoder;
        private OnLoadStateChangeListener onLoadStateChangeListener;
        private OnImageLoadListener onImageLoadListener;
        private volatile Rect clipImageRect;
        private volatile Bitmap bitmap;
        private volatile Throwable throwable;

        LoadBlockTask(Position position, BlockData blockData, int scale, int imageWidth, int imageHeight, BitmapRegionDecoder decoder, OnImageLoadListener onImageLoadListener, OnLoadStateChangeListener onLoadStateChangeListener) {
            this.blockData = blockData;
            this.scale = scale;
            this.position = position;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.decoder = decoder;
            this.onImageLoadListener = onImageLoadListener;
            this.onLoadStateChangeListener = onLoadStateChangeListener;
            if (DEBUG) {
                Log.d(TAG, "start LoadBlockTask position:" + position + " currentScale:" + scale);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (onLoadStateChangeListener != null) {
                onLoadStateChangeListener.onLoadStart(LOAD_TYPE_BLOCK, position);
            }
        }

        @Override
        protected void doInBackground() {
            if (DEBUG) {
                Log.d(TAG,"doInBackground："+Thread.currentThread()+" "+Thread.currentThread().getId());
            }
            int imageBlockSize = BASE_BLOCKSIZE * scale;
            int left = imageBlockSize * position.col;
            int right = left + imageBlockSize;
            int top = imageBlockSize * position.row;
            int bottom = top + imageBlockSize;
            if (right > imageWidth) {
                right = imageWidth;
            }
            if (bottom > imageHeight) {
                bottom = imageHeight;
            }
            clipImageRect = new Rect(left, top, right, bottom);
            try {
                BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    Bitmap bitmap = acquireBitmap();
                    decodingOptions.inBitmap = bitmap;
                    decodingOptions.inMutable = true;
                }
                decodingOptions.inSampleSize = scale;
                // 加载clipRect的区域的图片块
                bitmap = decoder.decodeRegion(clipImageRect, decodingOptions);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                throwable = e;
            } catch (Exception e) {
                if (DEBUG)
                    Log.d(TAG, position.toString() + " " + clipImageRect.toShortString());
                throwable = e;
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (bitmap != null) {
                bitmapPool.release(bitmap);
                bitmap = null;
            }
            decoder = null;
            blockData = null;
            onImageLoadListener = null;
            onLoadStateChangeListener = null;
            position = null;
            if (DEBUG) {
                Log.d(TAG, "onCancelled LoadBlockTask position:" + position + " currentScale:" + scale + " bit:");
            }
        }

        @Override
        protected void onPostExecute() {
            super.onPostExecute();
            if (DEBUG) {
                Log.d(TAG, "finish LoadBlockTask position:" + position + " currentScale:" + scale + " bitmap: " + (bitmap == null ? "" : bitmap.getWidth() + " bitH:" + bitmap.getHeight()));
            }
            blockData.task = null;
            if (bitmap != null) {
                blockData.bitmap = bitmap;
                blockData.realImageRect.set(0, 0, clipImageRect.width() / scale, clipImageRect.height() / scale);
                if (onImageLoadListener != null) {
                    onImageLoadListener.onBlockImageLoadFinished();
                }
            }
            if (onLoadStateChangeListener != null) {
                onLoadStateChangeListener.onLoadFinished(LOAD_TYPE_BLOCK, position, throwable == null, throwable);
            }
            decoder = null;
            blockData = null;
            onImageLoadListener = null;
            onLoadStateChangeListener = null;
            position = null;
        }
    }

    private static Bitmap acquireBitmap() {
        Bitmap bitmap = bitmapPool.acquire();
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(BASE_BLOCKSIZE, BASE_BLOCKSIZE, Bitmap.Config.ARGB_8888);
        }
        return bitmap;
    }

    private static class LoadThumbnailTask extends TaskQueue.Task {

        private int scale;
        private int imageWidth;
        private int imageHeight;
        private BitmapRegionDecoder decoder;
        private LoadData loadData;
        private OnLoadStateChangeListener onLoadStateChangeListener;
        private OnImageLoadListener onImageLoadListener;
        private volatile Bitmap bitmap;
        private volatile Throwable throwable;

        LoadThumbnailTask(LoadData loadData, BitmapRegionDecoder decoder, int thumbnailScale, int imageWidth, int imageHeight, OnImageLoadListener onImageLoadListener, OnLoadStateChangeListener onLoadStateChangeListener) {
            this.loadData = loadData;
            this.scale = thumbnailScale;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.decoder = decoder;
            this.onImageLoadListener = onImageLoadListener;
            this.onLoadStateChangeListener = onLoadStateChangeListener;
            if (DEBUG)
                Log.d(TAG, "LoadThumbnailTask LoadThumbnailTask thumbnailScale:" + thumbnailScale);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (onLoadStateChangeListener != null) {
                onLoadStateChangeListener.onLoadStart(LOAD_TYPE_THUMBNAIL, null);
            }
        }

        @Override
        protected void doInBackground() {
            BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
            decodingOptions.inSampleSize = scale;
            try {
                bitmap = decoder.decodeRegion(new Rect(0, 0, imageWidth, imageHeight),
                        decodingOptions);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                throwable = e;
            } catch (Exception e) {
                e.printStackTrace();
                throwable = e;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            onImageLoadListener = null;
            onLoadStateChangeListener = null;
            loadData = null;
            decoder = null;
            if (DEBUG) {
                Log.d(TAG, "onCancelled LoadThumbnailTask thumbnailScale:" + scale);
            }
        }

        @Override
        protected void onPostExecute() {
            super.onPostExecute();
            if (DEBUG) {
                Log.d(TAG, "LoadThumbnailTask bitmap:" + bitmap + " currentScale:" + scale + " bitW:" + (bitmap == null ? "" : bitmap.getWidth() + " bitH:" + bitmap.getHeight()));
            }
            loadData.thumbnailBlockData.task = null;
            if (bitmap != null) {
                if (loadData.thumbnailBlockData == null) {
                    loadData.thumbnailBlockData = new BlockData();
                }
                loadData.thumbnailBlockData.bitmap = bitmap;
                if (onImageLoadListener != null) {
                    onImageLoadListener.onBlockImageLoadFinished();
                }
            }
            if (onLoadStateChangeListener != null) {
                onLoadStateChangeListener.onLoadFinished(LOAD_TYPE_THUMBNAIL, null, throwable == null, throwable);
            }
            onImageLoadListener = null;
            onLoadStateChangeListener = null;
            loadData = null;
            decoder = null;
        }
    }

    public interface OnImageLoadListener {

        void onBlockImageLoadFinished();

        void onLoadImageSize(int imageWidth, int imageHeight);

        void onLoadFail(Exception e);
    }

    public interface OnLoadStateChangeListener {

        void onLoadStart(int loadType, Object param);

        void onLoadFinished(int loadType, Object param, boolean success, Throwable throwable);
    }

    public static final int LOAD_TYPE_INFO = 0;
    public static final int LOAD_TYPE_THUMBNAIL = 1;
    public static final int LOAD_TYPE_BLOCK = 2;
}
