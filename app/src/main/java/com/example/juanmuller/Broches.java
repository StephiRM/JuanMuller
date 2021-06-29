package com.example.juanmuller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.RequiresApi;

public class Broches implements Parcelable {
    private String descripcion;
    private String codigo;
    //private String codExterno;
    private double precio;
    //private String idproducto;
    private int cantidad;
    //private String dato;
    private String image;
    //private Bitmap imagen;
    private boolean estado;

    public Broches(){}

    /*public Broches(String descripcion, String codigo, String codExterno, double precio, String idproducto, Bitmap imagen) {
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.codExterno = codExterno;
        this.precio = precio;
        this.idproducto = idproducto;
        this.imagen = imagen;
    }*/

    public Broches(String descripcion, String codigo, double precio, String image) {
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.precio = precio;
        this.image = image;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Broches(Parcel in) {
        descripcion = in.readString();
        codigo = in.readString();
        //codExterno = in.readString();
        precio = in.readDouble();
        //idproducto = in.readString();
        cantidad = in.readInt();
        //dato = in.readString();
        image = in.readString();
        //imagen = in.readParcelable(Bitmap.class.getClassLoader());
        estado = in.readByte()!=0;
    }

    public static final Creator<Broches> CREATOR = new Creator<Broches>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Broches createFromParcel(Parcel in) {
            return new Broches(in);
        }

        @Override
        public Broches[] newArray(int size) {
            return new Broches[size];
        }
    };


    /*public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;

        try{
            byte[] byteCode = Base64.decode(dato, Base64.DEFAULT);
            //this.imagen = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);

            int alto=200;
            int ancho=200;

            Bitmap foto = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.imagen = Bitmap.createScaledBitmap(foto, alto, ancho, true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }*/

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    /*public String getCodExterno() {
        return codExterno;
    }*/

    public double getPrecio() {
        return precio;
    }

    /*public String getIdproducto() {
        return idproducto;
    }*/

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /*public void setCodExterno(String codExterno) {
        this.codExterno = codExterno;
    }*/

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /*public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }*/

    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(descripcion);
        dest.writeString(codigo);
        //dest.writeString(codExterno);
        dest.writeDouble(precio);
        //dest.writeString(idproducto);
        dest.writeInt(cantidad);
        //dest.writeString(dato);
        dest.writeString(image);
        //dest.writeParcelable(imagen, flags);
        dest.writeByte((byte)(estado ? 1 : 0));
    }
}
