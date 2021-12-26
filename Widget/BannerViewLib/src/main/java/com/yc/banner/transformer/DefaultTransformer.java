

package com.yc.banner.transformer;

import android.view.View;

public class DefaultTransformer extends ABaseTransformer {

	/**
	 * 参照：https://github.com/ToxicBakery/ViewPagerTransforms
	 */
	@Override
	protected void onTransform(View view, float position) {
	}

	@Override
	public boolean isPagingEnabled() {
		return true;
	}

}
