package com.choi.notice.persistence;

public interface SnsDetails<T extends SnsDetails<T>> {

	T getDetail();
}
