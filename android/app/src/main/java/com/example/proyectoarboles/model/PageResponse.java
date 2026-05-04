package com.example.proyectoarboles.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PageResponse<T> {

    @SerializedName("content")
    private List<T> content;

    @SerializedName("totalElements")
    private int totalElements;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("number")
    private int number;

    @SerializedName("size")
    private int size;

    public List<T> getContent() { return content; }
    public int getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public int getNumber() { return number; }
    public int getSize() { return size; }
}
