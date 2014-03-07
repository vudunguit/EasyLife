/*
 * Copyright (C) 2011 by Anton Novikau
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package lib.application;

import covisoft.android.EasyLife.R;

/**
 * Simple wrapper to pass the async task result or error type from background
 * thread to UI.
 * 
 * @author Anton Novikov
 */
public class AsyncTaskResult<Result> {

	private Result result;

	private final int errorCode;

	/**
	 * Creates an instance of AsyncTaskResult.
	 * 
	 * @param result
	 *            result of successful async task executing.
	 */
	public AsyncTaskResult(Result result) {
		this.result = result;
		this.errorCode = R.id.no_errors_code;
	}

	/**
	 * Creates an instance of AsyncTaskResult.
	 * 
	 * @param errorCode
	 *            error code that indicates what happened in execution thread.
	 */
	public AsyncTaskResult(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public Result getResult() {
		return result;
	}
}
