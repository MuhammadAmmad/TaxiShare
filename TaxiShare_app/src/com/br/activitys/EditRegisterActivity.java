/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.br.activitys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

import com.br.entidades.NovaPessoaApp;
import com.br.entidades.WSTaxiShare;
import com.br.sessions.SessionManagement;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.Toast;


public class EditRegisterActivity extends Activity {
	//botoes
	Button btnSalvar;
	Button btnEditVoltar;

	//dados pessoa
	EditText textNome;
	EditText textEmail;
	EditText textCelular;
	EditText textDDD;	
	Spinner spinnerSexo;
	EditText textNick;
	DatePicker dateDataNascimento;
	SessionManagement session;

	private static String pessoaID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_register);

		session = new SessionManagement(getApplicationContext());

		//Importando os campos da pessoa
		textNome = (EditText) findViewById(R.id.editNome);
		textNick = (EditText) findViewById(R.id.editNick);
		textEmail = (EditText) findViewById(R.id.editEmail);
		textDDD = (EditText) findViewById(R.id.editDDD);
		spinnerSexo = (Spinner) findViewById(R.id.editSexo);
		textCelular = (EditText) findViewById(R.id.editCelular);
		dateDataNascimento = (DatePicker) findViewById(R.id.editDateDataNascimemto);

		//Importando bot�es
		btnSalvar = (Button) findViewById(R.id.btnEditSalvar);
		btnEditVoltar = (Button) findViewById(R.id.btnEditVoltar);

		//Checa se o usuario esta logado
		session.checkLogin();

		//Recupera os dados do usuario na sess�o
		HashMap<String, String> user = session.getUserDetails();         

		//Pega as variaveis do usuario
		String nome = user.get(SessionManagement.KEY_NAME);
		String email = user.get(SessionManagement.KEY_EMAIL);
		String nick = user.get(SessionManagement.KEY_NICK);
		String sexo = user.get(SessionManagement.KEY_SEXO);
		String ddd = user.get(SessionManagement.KEY_DDD);
		String celular = user.get(SessionManagement.KEY_CELULAR);
		String datanasc = user.get(SessionManagement.KEY_DATANASC);

		//Setando id
		pessoaID = user.get(SessionManagement.KEY_PESSOAID);

		//Try para preencher o combo de sexo
		try {

			//Definindo Lista de sexos
			List<String> sexos = new ArrayList<String>();
			sexos.add("Masculino");
			sexos.add("Feminino");			

			//Colocando lista de sexos no spinner
			ArrayAdapter<String> adapterSexo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexos);
			adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerSexo.setAdapter(adapterSexo);

			//Retorna a posi��o do sexo no array e seta no spinner
			int spinnerPosition = adapterSexo.getPosition(sexo);
			spinnerSexo.setSelection(spinnerPosition);

			Log.i("Spinners preenchidos taxi", "");	


		}catch (Exception e1) {
			Log.i("Preenchendo Sppiners Exception taxi", e1 + "");	
		}

		// displaying user data
		textNome.setText(nome);
		textDDD.setText(ddd);
		textCelular.setText(celular);
		textEmail.setText(email);
		textNick.setText(nick);		

		try{
			//convertendo a data para int
			String year =datanasc.substring(6, 10);
			String month = datanasc.substring(3, 5);
			String day = datanasc.substring(0, 2);

			//convertendo a data para int
			int yearInt = Integer.parseInt(year);
			int monthInt = Integer.parseInt(month);
			int dayInt = Integer.parseInt(day);

			dateDataNascimento.updateDate(yearInt, monthInt - 1, dayInt);
		}
		catch(Exception e)
		{
			Log.i("Exception ao montar a data taxi", e + " -->" + e.getMessage());

		}


		new WSTaxiShare();		

		//Acao do botao cadastrar
		btnSalvar.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				//Pegando as informa��es de pessoas
				String nome = textNome.getText().toString();
				String nick = textNick.getText().toString();
				String email = textEmail.getText().toString();
				String ddd = textDDD.getText().toString();
				String celular = textCelular.getText().toString();

				//Montando data de nascimento
				int year = dateDataNascimento.getYear();
				int month = dateDataNascimento.getMonth();
				int day = dateDataNascimento.getDayOfMonth();
				String dataNascimento = day + "/" + month+ "/" +year;

				String sexo = spinnerSexo.getSelectedItem().toString();				

				//Criando objeto pessoa e objeto login
				NovaPessoaApp pessoaApp = new NovaPessoaApp();

				//Definindo as paradas em pessoa				
				pessoaApp.setId(Long.parseLong(pessoaID));
				pessoaApp.setNome(nome);
				pessoaApp.setNick(nick);
				pessoaApp.setCelular(celular);
				pessoaApp.setDataNascimento(dataNascimento);
				pessoaApp.setDdd(ddd);
				pessoaApp.setEmail(email);
				pessoaApp.setSexo(sexo);

				WSTaxiShare ws = new WSTaxiShare();
				//Acessando o WS para fazer a altera��o
				try 
				{
					//Chama o metodo de editar e passa a pessoa no parametro
					String respostaWs = ws.editarCadastro(pessoaApp);
					//Cria um obj JSON com a resposta
					JSONObject respostaWsJSON = new JSONObject(respostaWs);
					//Imprime na tela a descri��o da a��o
					gerarToast(respostaWsJSON.getString("descricao"));
					
					session.createLoginSession(pessoaID, nome,  email,  sexo,  dataNascimento,  nick,  ddd,  celular);
					
					//Transfere para a pagina de dashboard
					Intent i = new Intent(getApplicationContext(),
							DashboardActivity.class);
					startActivity(i);
					finish();

				} 
				catch (Exception e) {
					gerarToast("Erro ao alterar!");
					Log.i("Exception alterar taxi", e + "");
				}
			}
		});


		//Acao do botao voltar
		btnEditVoltar.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						DashboardActivity.class);
				startActivity(i);
				finish();			
			}
		});
	}


	private void gerarToast(CharSequence message) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast
				.makeText(getApplicationContext(), message, duration);
		toast.show();
	}

}


