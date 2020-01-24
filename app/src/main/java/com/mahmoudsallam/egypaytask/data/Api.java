package com.mahmoudsallam.egypaytask.data;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
    @POST("/values")
    Observable<String> submitData(@Body Map<String, String> map);

}
