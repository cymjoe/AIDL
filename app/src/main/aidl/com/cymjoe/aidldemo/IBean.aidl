// IBean.aidl
package com.cymjoe.aidldemo;
parcelable TestBean;
// Declare any non-default types here with import statements

interface IBean {
    List<TestBean>getAll();
    void add(in TestBean bean);
}
