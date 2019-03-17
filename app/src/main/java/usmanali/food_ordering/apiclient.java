package usmanali.food_ordering;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class apiclient {

      public static final String BASE_URL = "https://studio6292.000webhostapp.com/";
      private static Retrofit retrofit = null;
      public static Retrofit getClient() {
          Gson gson = new GsonBuilder()
                  .setLenient()
                  .create();

          if (retrofit==null) {
                              retrofit = new Retrofit.Builder()
                                      .baseUrl(BASE_URL)
                                      .addConverterFactory(GsonConverterFactory.create(gson))
                                      .build();
                          }
                      return retrofit;
                  }

    }
