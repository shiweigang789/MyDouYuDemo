package com.swg.mydouyudemo.model;

import com.swg.mydouyudemo.base.BaseModel;
import com.swg.mydouyudemo.base.BasePresenter;
import com.swg.mydouyudemo.base.BaseView;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swg on 2017/11/21.
 */
@SuppressWarnings("unchecked")
public class ContractProxy {

    private Map<Class, Object> mObjects;

    private static final ContractProxy mInstance = new ContractProxy();

    private ContractProxy() {
        mObjects = new HashMap<>();
    }

    public static ContractProxy getInstance() {
        return mInstance;
    }

    /**
     * Presenter
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型.
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    public static Class<BasePresenter> getPresenterClazz(final Class clazz, final int index) {

        // 返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return BasePresenter.class;
        }

        // 返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return BasePresenter.class;
        }

        if (!(params[index] instanceof Class)) {
            return BasePresenter.class;
        }

        return (Class) params[index];

    }

    /**
     * Model
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型.
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    public static Class<BaseModel> getModelClazz(final Class clazz, final int index) {
        // 返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return BaseModel.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return BaseModel.class;
        }

        if (!(params[index] instanceof Class)) {
            return BaseModel.class;
        }

        return (Class<BaseModel>) params[index];
    }

    /**
     * 获取presenter
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T presenter(Class clazz) {
        if (!mObjects.containsKey(clazz)) {
            initInstance(clazz);
        }
        BasePresenter presenter = null;
        try {
            presenter = (BasePresenter) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) presenter;
    }

    /**
     * 进行初始化
     *
     * @param clazz
     */
    private void initInstance(Class clazz) {
        try {
            mObjects.put(clazz, clazz.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定View
     *
     * @param view
     * @param presenter
     * @param <V>
     * @return
     */
    public <V> V bindView(BaseView view, BasePresenter presenter) {
        if (view != presenter.getView()) {
            if (presenter.getView() != null) {
                presenter.detachView();
            }
            presenter.attachView(view);
        }
        return (V) view;
    }

    /**
     * 绑定Presenter
     *
     * @param clazz
     * @param view
     * @param <T>
     * @return
     */
    public <T> T bindPresenter(Class clazz, BaseView view) {
        if (!mObjects.containsKey(clazz)) {
            initInstance(clazz);
        }
        BasePresenter presenter = null;
        try {
            presenter = ((BasePresenter) clazz.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (view != presenter.getView()) {
            if (presenter.getView() != null) {
                presenter.detachView();
            }
            presenter.attachView(view);
        }
        return (T) presenter;
    }

    /**
     * 绑定Model
     *
     * @param clazz
     * @param presenter
     * @param <M>
     * @return
     */
    public <M> M bindModel(Class clazz, BasePresenter presenter) {
        if (!mObjects.containsKey(clazz)) {
            initInstance(clazz);
        }
        BaseModel model = null;
        try {
            model = (BaseModel) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (model != presenter.getModel()) {
            if (presenter.getModel() != null) {
                presenter.detachModel();
            }
            presenter.attachModel(model);
        }
        return (M) model;
    }

    /**
     * 解除绑定View
     *
     * @param view
     * @param presenter
     */
    public void unbindView(BaseView view, BasePresenter presenter) {
        if (presenter.getView() != null) {
            if (view == presenter.getView()) {
                presenter.detachView();
            }
        }
    }

    /**
     * 解除绑定Model
     *
     * @param clzz
     * @param presenter
     */
    public void unbindModel(Class clzz, BasePresenter presenter) {
        if (mObjects.containsKey(clzz)) {
            BaseModel model = null;
            try {
                model = ((BaseModel) clzz.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (presenter.getModel() != null) {
                if (model == presenter.getModel()) {
                    presenter.detachModel();
                    mObjects.remove(clzz);
                }
            }

        }
    }

}