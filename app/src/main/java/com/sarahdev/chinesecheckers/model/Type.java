package com.sarahdev.chinesecheckers.model;

public enum Type {
    Human("human"), Computer("computer"), NoOne("noone");
    String nom;

    Type(String nom) {
        this.nom = nom;
    }
}
