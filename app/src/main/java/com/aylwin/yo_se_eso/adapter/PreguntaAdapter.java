package com.aylwin.yo_se_eso.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aylwin.yo_se_eso.R;
import com.aylwin.yo_se_eso.modelo.response.Pregunta;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PreguntaAdapter  extends RecyclerView.Adapter<PreguntaAdapter.PreguntaAdapterViewHolder>{

    ArrayList<Pregunta> listPregunta;
    OnItemClickListener listener;

    public PreguntaAdapter(ArrayList<Pregunta> listPregunta) {
        this.listPregunta = listPregunta;
    }

    @NonNull
    @Override
    public PreguntaAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pregunta,parent,false);
        return new PreguntaAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreguntaAdapterViewHolder holder, int position) {

        //3 items
        Pregunta pregunta = listPregunta.get(position);

        holder.tv_nombre.setText(pregunta.getNombre());
        holder.tv_fecha.setText(pregunta.getFecha());
        holder.tv_tema.setText(pregunta.getTema());
        holder.tv_nombreUsuario.setText(pregunta.getNombreUsuario());
        holder.tv_fecha.setText(pregunta.getFecha());


        String url = "http://aylwin100-001-site1.itempurl.com/"+pregunta.getRutaImagen();

        Picasso.get().load(url).error(R.drawable.error_center_x).into(holder.img_foto);


    }

    @Override
    public int getItemCount() {
        return listPregunta.size();
    }

    public Pregunta obtenerPregunta(int position) {

        return listPregunta.get(position);
    }

    public void eliminarPregunta(int position) {

        listPregunta.remove(position);
        notifyItemRemoved(position);
    }


    public class PreguntaAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nombre, tv_fecha, tv_tema, tv_nombreUsuario;//tv_rutaImagen
        ImageView img_foto;

        public PreguntaAdapterViewHolder(@NonNull View itemView) {
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
                        listener.onItemClick(listPregunta.get(position));
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(Pregunta pregunta);
    }

    //Nexo entre la actividad y el adaptador
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
