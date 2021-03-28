/*
 *
 *   Copyright Ericsson 2020
 *
 *   The copyright to the computer program(s) herein is the property of
 *   Ericsson Inc. The programs may be used and/or copied only with written
 *   permission from Ericsson Inc. or in accordance with the terms and
 *   conditions stipulated in the agreement/contract under which the
 *   program(s) have been supplied.
 *
 *
 */
package com.shunyi.laboratory.schema;

/**
 * @Author: Simeon Chen
 * @Date: 2020/11/28
 *
 * @Description:  Base checker
 */
public interface Callback<P,R> {
    /**
     * The <code>call</code> method is called when required, and is given a
     * single argument of type P, with a requirement that an object of type R
     * is returned.
     *
     * @param param The single argument upon which the returned value should be
     *      determined.
     * @return An object of type R that may be determined based on the provided
     *      parameter value.
     */
    public R call(P param);
}