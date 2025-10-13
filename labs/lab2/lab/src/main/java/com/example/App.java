package com.example;










public class App 
{
    public static void main( String[] args )
    {
        DocumentReader dr = new DocumentReader();
        System.out.println(dr.loadDocuments("corpus"));
    }
}
