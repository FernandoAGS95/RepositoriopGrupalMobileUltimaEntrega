package com.example.app_grupo7.repository

import com.example.app_grupo7.model.Perfume
import com.example.app_grupo7.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object PerfumeRepository {
    fun getCatalogo(): List<Perfume> = listOf(
        Perfume("p1","Bleu de Chanel","Chanel",129990,100,"Aromático amaderado, versátil.", imageRes = R.drawable.bleuu),
        Perfume("p2","Sauvage","Dior",119990,100,"Fresco especiado, muy proyectón.", imageRes = R.drawable.sauvage),
        Perfume("p3","Azzaro The Most Wanted Parfum","Azzaro",99990,100,"Dulce especiado, nocturno.", imageRes = R.drawable.tmw_parfum),
        Perfume("p4","Acqua di Gio Profumo","Armani",134990,75,"Acuático–incienso, elegante.", imageRes = R.drawable.profumo),
        Perfume("p5","Light Blue","Dolce & Gabbana",89990,100,"Cítrico fresco, diario.", imageRes = R.drawable.lightblue),
        Perfume("p6","Le Male Elixir","Jean Paul Gaultier",114990,100,"Dulce avainillado, nocturno.", imageRes = R.drawable.lemalelixir),
        Perfume("p7","Invictus","Paco Rabanne",94990,100,"Dulce fresco, juvenil.", imageRes = R.drawable.invictus),
        Perfume("p8","The One","Dolce & Gabbana",99990,100,"Ámbar especiado, cita.", imageRes = R.drawable.theone),
        Perfume("p9","Allure Homme Sport","Chanel",124990,100,"Cítrico–almizclado, deportivo.", imageRes = R.drawable.allure),
        Perfume("p10","Oud Wood","Tom Ford",199990,100,"Amaderado oud, nicho.", imageRes = R.drawable.oud),
        Perfume("p11","CK One","Calvin Klein",64990,200,"Unisex, cítrico clásico.", imageRes = R.drawable.ck),
        Perfume("p12","L’Homme","YSL",119990,100,"Amaderado fresco, oficina.", imageRes = R.drawable.lhomme)
    )
}
