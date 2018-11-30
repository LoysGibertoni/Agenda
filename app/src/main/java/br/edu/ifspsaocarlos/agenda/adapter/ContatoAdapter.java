package br.edu.ifspsaocarlos.agenda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.model.Contato;


public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder> {

    private static List<Contato> contatos;
    private Context context;

    private static ItemClickListener clickListener;
    private static ItemClickListener favoritoClickListener;

    public ContatoAdapter(List<Contato> contatos, Context context) {
        this.contatos = contatos;
        this.context = context;
    }

    @Override
    public ContatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contato_celula, parent, false);
        return new ContatoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ContatoViewHolder holder, int position) {
        final Contato contato = contatos.get(position);
       holder.nome.setText(contato.getNome());
       holder.favorito.setImageResource(contato.isFavorito() ? R.drawable.ic_star : R.drawable.ic_star_border);
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public void setFavoritoClickListener(ItemClickListener itemFavoritoClickListener) {
        favoritoClickListener = itemFavoritoClickListener;
    }

    public  class ContatoViewHolder extends RecyclerView.ViewHolder {
        final TextView nome;
        final ImageView favorito;

        ContatoViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.nome);
            favorito = view.findViewById(R.id.favorito);

            nome.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
            favorito.setOnClickListener(v -> {
                if (favoritoClickListener != null) {
                    favoritoClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }


    public interface ItemClickListener {
        void onItemClick(int position);
    }

}


