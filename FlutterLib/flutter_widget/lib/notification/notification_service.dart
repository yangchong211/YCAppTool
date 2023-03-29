/**
 * 通知栏服务
 */
abstract class NotificationService<T> {

  /**
   * 有新通知需要展示
   */
  void show(T notification);

  /**
   * 需要移除指定的通知
   */
  void cancel(T notification);

  /**
   * 移除指定 ID 的通知
   */
  void cancelById(String id);

}