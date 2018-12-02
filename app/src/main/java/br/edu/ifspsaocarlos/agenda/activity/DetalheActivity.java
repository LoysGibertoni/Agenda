package br.edu.ifspsaocarlos.agenda.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;


public class DetalheActivity extends AppCompatActivity {
    private Contato c;
    private ContatoDAO cDAO;
    private DatePickerDialog datePickerDialog;
    private EditText nomeText;
    private EditText foneText;
    private EditText fone2Text;
    private EditText emailText;
    private EditText aniversarioText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindViews();
        criarDatePickerDialog();

        if (getIntent().hasExtra("contato"))
        {
            c = (Contato) getIntent().getSerializableExtra("contato");
            nomeText.setText(c.getNome());
            foneText.setText(c.getFone());
            fone2Text.setText(c.getFone2());
            emailText.setText(c.getEmail());
            if (!TextUtils.isEmpty(c.getAniversario())) {
                try {
                    final Date dataAniversario = new SimpleDateFormat("dd/MM", Locale.getDefault()).parse(c.getAniversario());
                    final Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTime(dataAniversario);
                    calendar.set(Calendar.YEAR, 2020);
                    datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    aniversarioText.setText(c.getAniversario());
                } catch (ParseException ignored) { }
            }
            int pos =c.getNome().indexOf(" ");
            if (pos==-1)
                pos=c.getNome().length();
            setTitle(c.getNome().substring(0,pos));
        }
        cDAO = new ContatoDAO(this);
    }

    private void bindViews() {
        nomeText = findViewById(R.id.editTextNome);
        foneText = findViewById(R.id.editTextFone);
        fone2Text = findViewById(R.id.editTextFone2);
        emailText = findViewById(R.id.editTextEmail);
        aniversarioText = findViewById(R.id.editTextAniversario);
        aniversarioText.setOnClickListener(v -> datePickerDialog.show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        if (!getIntent().hasExtra("contato"))
        {
            MenuItem item = menu.findItem(R.id.delContato);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvarContato:
                salvar();
                return true;
            case R.id.delContato:
                apagar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void criarDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                (view, year, month, dayOfMonth) -> aniversarioText.setText(String.format(Locale.getDefault(), "%02d/%02d", dayOfMonth, month + 1)),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        {
            @Override
            protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                final View yearView = getDatePicker().findViewById(getResources().getIdentifier("year","id","android"));
                if (yearView != null) {
                    yearView.setVisibility(View.GONE);
                }
                calendar.set(Calendar.YEAR, 2020);
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                getDatePicker().setMinDate(calendar.getTimeInMillis());
                calendar.set(Calendar.MONTH, 11);
                calendar.set(Calendar.DAY_OF_MONTH, 31);
                getDatePicker().setMaxDate(calendar.getTimeInMillis());
            }
        };
    }

    private void apagar()
    {
        cDAO.apagaContato(c);

        Intent resultIntent = new Intent();
        setResult(3,resultIntent);
        finish();
    }

    private void salvar()
    {
        if (c==null) {
            c = new Contato();
        }
        
        c.setNome(nomeText.getText().toString());
        c.setFone(foneText.getText().toString());
        c.setFone2(fone2Text.getText().toString());
        c.setEmail(emailText.getText().toString());
        c.setAniversario(aniversarioText.getText().toString());

        cDAO.salvaContato(c);
        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }
}

