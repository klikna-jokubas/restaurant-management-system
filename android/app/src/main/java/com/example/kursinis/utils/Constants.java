package com.example.kursinis.utils;

public class Constants {
    public static final String HOME_URL = "http://10.0.2.2:8080/";
    public static final String VALIDATE_USER_URL = HOME_URL + "validateUser";

    public static final String GET_ALL_RESTAURANTS_URL = HOME_URL + "getAllRestaurants";

    public static final String REGISTER_USER_URL = HOME_URL + "registerUser";

    public static final String GET_ORDERS_BY_USER = HOME_URL + "getOrderByUserId/";

    public static final String GET_MESSAGES_BY_ORDER = HOME_URL + "chat/messages/";

    public static final String SEND_MESSAGE = HOME_URL + "sendMessage";

    public static final String GET_MENU_BY_RESTAURANT = HOME_URL + "restaurant/";

    public static final String INSERT_ORDER_URL = HOME_URL + "insertOrder";

    public static final String UPDATE_USER = HOME_URL + "updateUser/";

    public static final String  GET_NEW_ORDERS_URL = HOME_URL + "driver/orders/available";

    public static final String  TAKE_ORDER_URL= HOME_URL + "driver/orders/take";

    public static final String GET_DRIVER_ORDERS_URL = HOME_URL + "driver/orders/";

    public static final String MARK_ORDER_DELIVERED = HOME_URL + "driver/order/deliver";

    public static final String ADD_REVIEW = HOME_URL + "rating/add";


}
