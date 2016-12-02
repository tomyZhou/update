package cn.hugeterry.updatefun.module;

/**
 * Created by wangLianJun on 2016/12/1.
 * 邮箱：695301501@qq.com
 *
 * @desc
 * @tools
 */

public interface UpdateListener {

    /**
     * 是否更新
     * @param flag   1 更新 2 网络不通   3不更新
     */
    public void OnUpdateListener(int flag);

}
