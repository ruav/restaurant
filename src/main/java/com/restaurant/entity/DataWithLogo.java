package com.restaurant.entity;

public interface DataWithLogo<T extends Data> extends Data{
   T getLogo();
   void setLogo(T logo);
}
