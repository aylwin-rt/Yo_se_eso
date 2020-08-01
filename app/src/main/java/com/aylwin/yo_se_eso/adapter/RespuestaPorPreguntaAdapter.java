package com.aylwin.yo_se_eso.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aylwin.yo_se_eso.R;
import com.aylwin.yo_se_eso.modelo.response.Respuesta;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RespuestaPorPreguntaAdapter extends RecyclerView.Adapter<RespuestaPorPreguntaAdapter.RespuestaPorTuPreguntaAdapterViewHolder> {
    ArrayList<Respuesta> listRespuesta;
    RespuestaPorPreguntaAdapter.OnItemClickListener listener;

    public RespuestaPorPreguntaAdapter(ArrayList<Respuesta> listRespuesta) {
        this.listRespuesta = listRespuesta;
    }


    @NonNull
    @Override
    public RespuestaPorPreguntaAdapter.RespuestaPorTuPreguntaAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_respuesta,parent,false);
        return new RespuestaPorPreguntaAdapter.RespuestaPorTuPreguntaAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RespuestaPorPreguntaAdapter.RespuestaPorTuPreguntaAdapterViewHolder holder, int position) {

        Respuesta respuesta = listRespuesta.get(position);
        holder.tv_nombre.setText(respuesta.getNombre());
        holder.tv_fecha.setText(respuesta.getFecha());
        holder.tv_nombreUsuario.setText(respuesta.getNombreUsuario());
        //holder.tv_fecha.setText(respuesta.getFecha());


        String url = "http://aylwin100-001-site1.itempurl.com/"+respuesta.getRutaImagen();

        Picasso.get().load(url).error(R.drawable.error_center_x).into(holder.img_foto);

    }


    @Override
    public int getItemCount() {
        return listRespuesta.size();
    }



    public Respuesta obtenerRespuesta(int position) {

        return listRespuesta.get(position);
    }

/*
    public void eliminarPregunta(int position) {

        listPregunta.remove(position);
        notifyItemRemoved(position);
    }

     */


    public class RespuestaPorTuPreguntaAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nombre, tv_fecha, tv_tema, tv_nombreUsuario;//tv_rutaImagen
        ImageView img_foto;

        public RespuestaPorTuPreguntaAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_nombre = itemView.findViewById(R.id.tv_nombre);
            tv_fecha = itemView.findViewById(R.id.tv_fecha);
            tv_tema = itemView.findViewById(R.id.tv_tema);
            tv_nombreUsuario = itemView.findViewById(R.id.tv_nombreUsuario);
            img_foto = itemView.findViewById(R.id.img_foto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (listener!=null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(listRespuesta.get(position));
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(Respuesta respuesta);
    }


    //Nexo entre la actividad y el adaptador
    public void setOnItemClickListener(RespuestaPorPreguntaAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

}