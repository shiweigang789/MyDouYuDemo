package com.swg.mydouyudemo.base;

/**
 * Created by swg on 2017/11/21.
 */

public interface CommonView {

    /**
     * 提示成功信息
     * @param msg
     */
    void showSuccessWithStatus(String msg);

    /**
     * 提示错误信息
     * @param msg
     */
    void showErrorWithStatus(String msg);

    /**
     * 提示消息
     * @param msg
     */
    void showInfoWithStatus(String msg);

    /**
     * 进度框
     * @param msg
     */
    void showWithProgress(String msg);

    /**
     * 取消显示
     */
    void dismiss();

}
