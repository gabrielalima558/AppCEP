package br.com.consultacep.cep;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

   EditText txt_cep;
    EditText txt_rua;
    EditText txt_bairro;
    EditText txt_estado;
    Button btn_consultar;
    String cep;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_cep = (EditText)findViewById(R.id.txt_cep);
        txt_rua = (EditText)findViewById(R.id.txt_rua);
        txt_bairro = (EditText)findViewById(R.id.txt_bairro);
        txt_estado = (EditText)findViewById(R.id.txt_estado);
        btn_consultar = (Button)findViewById(R.id.btn_consultar);

        context = this;
    }

    public void Consultar(View view) {

         cep = txt_cep.getText().toString();


        /*essa linha executa o doInbackgound*/
        new BuscaCepTask().execute();

        /*String link = String.format("https://viacep.com.br/ws/%s/json/", cep);

        String retornoJson = HttpConnection.get(link); tirei essas linha daqui pq ia dar erro nas threads que executariam juntas */


    }


    private class  BuscaCepTask extends AsyncTask<Void, Void, Void>{

        String retornoJson;

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progress = ProgressDialog.show(context,"Aguarde", "Estamos trabalhando na sua solicitação (:");
        }

        /* doInBackground por causa das threads que não podem executar justas*/
        @Override
        protected Void doInBackground(Void... params) {


            String link = String.format("http://viacep.com.br/ws/%s/json/", cep);

            retornoJson = HttpConnection.get(link);

            /*
            retornoJson = "{\"cep\" : \"06160040\"," +
                    "\"logradouro\" : \"Elton Silva\"," +
                    "\"complemento\": \"Jandira\","+
                    "\"bairro\": \"Jandira\","+
                    "\"uf\": \"Jandira\","+
                    "\"unidade\": \"Jandira\","+
                    "\"ibge\": \"56655\","+
                    "\"gia\": \"0145\","+"}";*/

            /*Log.d é tipo um Sysout serve pra exibir no console*/
            Log.d("doInBackground", retornoJson);
            return null;
        }

        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);

            progress.dismiss();

            Gson gson = new Gson();
            Endereco end = gson.fromJson(retornoJson, Endereco.class);
            txt_bairro.setText(end.getBairro());
            txt_rua.setText(end.getLogradouro());
            txt_cep.setText(end.getCep());
            txt_estado.setText(end.getUf() + " - " + end.getLocalidade());


        }
    }
}
