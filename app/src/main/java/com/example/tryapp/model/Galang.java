package com.example.tryapp.model;

import java.io.Serializable;

public class Galang implements Serializable {

    String id_galang;
    String id_member;
    String judul;
    String nama_pasien;
    String menderita;
    String deskripsi;
    String tgl_mulai;
    String alamat;
    String tgl_selesai;
    String dana;
    String terkumpul;
    String gambar;
    String buktiSurat;
    String status;

    public Galang(String id_galang, String id_member, String judul, String nama_pasien, String menderita, String deskripsi,
                  String tgl_mulai, String alamat, String tgl_selesai, String dana, String terkumpul, String gambar, String buktiSurat, String status) {
        this.id_galang = id_galang;
        this.id_member = id_member;
        this.judul = judul;
        this.nama_pasien = nama_pasien;
        this.menderita = menderita;
        this.deskripsi = deskripsi;
        this.tgl_mulai = tgl_mulai;
        this.alamat = alamat;
        this.tgl_selesai = tgl_selesai;
        this.dana = dana;
        this.terkumpul = terkumpul;
        this.gambar = gambar;
        this.buktiSurat = buktiSurat;
        this.status = status;
    }

    public String getId_galang() {
        return id_galang;
    }

    public void setId_galang(String id_galang) {
        this.id_galang = id_galang;
    }

    public String getId_member() {
        return id_member;
    }

    public void setId_member(String id_member) {
        this.id_member = id_member;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getNama_pasien() {
        return nama_pasien;
    }

    public void setNama_pasien(String nama_pasien) {
        this.nama_pasien = nama_pasien;
    }

    public String getMenderita() {
        return menderita;
    }

    public void setMenderita(String menderita) {
        this.menderita = menderita;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTgl_mulai() {
        return tgl_mulai;
    }

    public void setTgl_mulai(String tgl_mulai) {
        this.tgl_mulai = tgl_mulai;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTgl_selesai() {
        return tgl_selesai;
    }

    public void setTgl_selesai(String tgl_selesai) {
        this.tgl_selesai = tgl_selesai;
    }

    public String getDana() {
        return dana;
    }

    public void setDana(String dana) {
        this.dana = dana;
    }

    public String getTerkumpul() {
        return terkumpul;
    }

    public void setTerkumpul(String terkumpul) {
        this.terkumpul = terkumpul;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getBuktiSurat() {
        return buktiSurat;
    }

    public void setBuktiSurat(String buktiSurat) {
        this.buktiSurat = buktiSurat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
