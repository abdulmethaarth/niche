package com.affinitity.niche.ui.dashboard;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("api/Api.php?apicall=videos")
 Call<JSONResponse> getJSON();
 //   Call<List<CategoryModel>> getUsers();
}
