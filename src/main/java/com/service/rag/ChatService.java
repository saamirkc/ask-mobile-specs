package com.service.rag;


public interface ChatService {


    void addPhoneSpecsInVectorDB();


    String retreiveDataFromVectorDB(String message);


}

