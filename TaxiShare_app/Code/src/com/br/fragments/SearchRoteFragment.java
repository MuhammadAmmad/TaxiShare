package com.br.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.br.activitys.R;
import com.br.entidades.EnderecoApp;
import com.br.entidades.PerimetroApp;
import com.br.entidades.RotaApp;
import com.br.network.WSTaxiShare;
import com.br.resources.GpsTracker;
import com.br.resources.MapUtils;
import com.br.resources.Utils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class SearchRoteFragment extends Fragment {

	// Google Map
	private GoogleMap googleMap;
	AQuery aQuery;
	private MapView mapView;
	private Bundle mBundle;
	private Button btnBusca;
	private EditText txtEndereco1;
	private EditText txtEndereco2;
	public Context context;
	EnderecoApp enderecoOrigem;
	EnderecoApp enderecoDestino;
	Address ori, dest;
	List<Address> destinoLista;
	List<Address> origemLista;
	MapUtils mapUtils;
	GpsTracker gps;
	View rootView;
	private boolean doubleBackToExitPressedOnce;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.rote_search, container, false);
		context = getActivity();
		gps = new GpsTracker(context);
		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			Utils.logException("SearchRoteFragment", "onCreateView", "", e);
		}

		setAtributes(rootView);
		centerMapOnMyLocation();
		setBtnAction();
		setMarker();

		verificaTamanhoTela();		
		

		return rootView;	
	}

	public Marker setMarker() {
		double latitude = gps.getLatitude();
		double longitude = gps.getLongitude();

		//		googleMap.addMarker(new MarkerOptions().position(new LatLng(-23.489839, -46.410520)).title("Marker"));

		Marker mark = googleMap.addMarker(new MarkerOptions()
		.position(new LatLng(latitude, longitude))
		.title("Voce est� aqui!")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.maker_azul)));

		mark.showInfoWindow();

		return mark;
	}

	public List<Address>  getListaDeEnderecos(String endereco) throws IOException {
		//este Adress aqui recebe um retorno do metodo geoCoder.getFromLocationName vc manipula este retorno pra pega as coordenadas
		List<Address> enderecos = new ArrayList<Address>(); 

		// esse Geocoder aqui � quem vai traduzir o endere�o de String para coordenadas double
		android.location.Geocoder geocoder = new android.location.Geocoder(context);

		enderecos = geocoder.getFromLocationName(endereco, 10000);

		return enderecos;
	}

	public CharSequence[] getListaConvertida(List<Address> enderecos){
		String[] strEnderecos = new String [enderecos.size()];

		for(int i = 0; i<enderecos.size(); i++){
			Address address = enderecos.get(i);

			String endereco = address.getThoroughfare();

			String numero = address.getSubThoroughfare() != null ? address.getSubThoroughfare() : "Sem numero" ;
			String bairro = address.getSubLocality() != null ? address.getSubLocality() : "Sem bairro" ;
			String cidade = address.getLocality() != null ? address.getLocality() : "Sem cidade" ;
			String estado = address.getAdminArea() != null ? address.getAdminArea() : "Sem estado";

			strEnderecos[i] = endereco + ", " + numero + ", " + bairro + " - " + cidade + " / " + estado ;		
		}	

		return strEnderecos;

	}

	public void setAtributes(View rootView){

		mapView = (MapView) rootView.findViewById(R.id.rote_search_map);
		
		mapView.onCreate(mBundle);

		
		if (googleMap == null) {
			googleMap = mapView.getMap();
			if (googleMap != null) {
			}
		}

//		googleMap.setTrafficEnabled(true);
		

		btnBusca = (Button) rootView.findViewById(R.id.rote_search_btn_buscar);

		txtEndereco1 = (EditText) rootView.findViewById(R.id.rote_search_txt_origem);
		txtEndereco2 = (EditText) rootView.findViewById(R.id.rote_search_txt_destino);

//		txtEndereco1.setText("rua quata 300");
//		txtEndereco2.setText("rua cervinho 191");
		aQuery = new AQuery(rootView.getContext());	

		mapUtils = new MapUtils(context, googleMap);
		

	}

	public void setBtnAction(){
		//acao do botao buscar
		btnBusca.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				//pega o texto dos campos
				String origem = txtEndereco1.getText().toString();
				String destino = txtEndereco2.getText().toString();

				boolean origemNumberTest = origem.matches(".*\\d.*") ;
				boolean destinoNumberTest = destino.matches(".*\\d.*") ;

				if(!origemNumberTest){
					txtEndereco1.setError("Informe o endere�o com n�mero");
				}
				if(!destinoNumberTest){
					txtEndereco2.setError("Informe o endere�o com n�mero");
				}

				if(origemNumberTest && destinoNumberTest){
					try {
						//recebe uma lista de endere�os com objetos ADDRESS
						origemLista = getListaDeEnderecos(origem);
						destinoLista = getListaDeEnderecos(destino);

						//Converte para uma lista de strings formatadas
						final CharSequence[] enderecosOrigem = getListaConvertida(origemLista);
						final CharSequence[] enderecosDestino = getListaConvertida(destinoLista);

						//Checa se houve retorno para os dois endere�os
						if(enderecosOrigem.length > 0 && enderecosDestino.length >0){
							//Cria os 2 popUps
							AlertDialog.Builder popupOrigem = new AlertDialog.Builder(context);
							final AlertDialog.Builder popupDestino = new AlertDialog.Builder(context);

							//Seta os titulos
							popupOrigem.setTitle("Selecione Origem");
							popupDestino.setTitle("Selecione Destino");

							//Define os itens da lista e coloca a��o no click da origem
							popupOrigem.setItems(enderecosOrigem, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//Define o objeto origem de acordo com a escolha na lista
									ori = origemLista.get(which);
									//Passa a bola para janela de escolha do destino
									popupDestino.show();
								}	
							});					

							//Define os itens da lista e coloca a��o no click do destino
							popupDestino.setItems(enderecosDestino, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {

									//Define o objeto destino de acordo com a escolha na lista
									dest = destinoLista.get(which);

									String enderecoOrigem = ori.getThoroughfare();
									String numeroOrigem = ori.getSubThoroughfare() != null ? ori.getSubThoroughfare() : "" ;
									String cidadeOrigem = ori.getLocality() != null ? ori.getLocality() : "" ;

									String enderecoDestino = dest.getThoroughfare();
									String numeroDestino = dest.getSubThoroughfare() != null ? dest.getSubThoroughfare() : "" ;
									String cidadeDestino = dest.getLocality() != null ? dest.getLocality() : "" ;

									//Agora definimos as longitudes e latitudes da origem e destino
									String textoOrigem = enderecoOrigem + ", " + numeroOrigem + " - " + cidadeOrigem;
									String textoDestino = enderecoDestino + ", " + numeroDestino + " - " + cidadeDestino;
									txtEndereco1.setText(textoOrigem);
									txtEndereco2.setText(textoDestino);
									double origemLatitude = ori.getLatitude();
									double origemLongitude = ori.getLongitude();

									double destinoLatitude = dest.getLatitude();
									double destinoLongitude = dest.getLongitude();		

									//Executa uma async task que ira no ws pegar a lista de rotas
									RouteListTask task = new RouteListTask(origemLatitude, origemLongitude, destinoLatitude, destinoLongitude);
									task.execute();								
								}
							});

							//Mostra a popup de origem primeiro
							popupOrigem.show();						

						}
						//Se um endere�o n�o deu retorno
						else{
							//seta o erro aonde a busca n�o deu retorno
							if(enderecosOrigem.length <= 0){
								txtEndereco1.setError("Seja mais especifico");
								txtEndereco1.setFocusable(true);
							}

							if(enderecosOrigem.length <= 0){
								txtEndereco1.setError("Seja mais especifico");
								txtEndereco1.setFocusable(true);
							}

							Utils.gerarToast(context, "Sem resultados");
						}

					} catch (Exception e) {
						Utils.logException("SerachRoteFragment", "setBtnActions", "", e);
						Utils.gerarToast(context, "Falha de rede! Tente novamente mais tarde.");
					}
				}
			}
		});
	}	

	//S� estamos usando esse metodo, ele retorna os 4 pontos para montar o perimetro.
	private double[] getBoundingBox(final double pLatitude, final double pLongitude, final int pDistanceInMeters) {

		final double[] boundingBox = new double[4];

		final double latRadian = Math.toRadians(pLatitude);

		final double degLatKm = 110.574235;
		final double degLongKm = 110.572833 * Math.cos(latRadian);
		final double deltaLat = pDistanceInMeters / 1000.0 / degLatKm;
		final double deltaLong = pDistanceInMeters / 1000.0 / degLongKm;

		final double minLat = pLatitude - deltaLat;
		final double minLong = pLongitude - deltaLong;
		final double maxLat = pLatitude + deltaLat;
		final double maxLong = pLongitude + deltaLong;

		boundingBox[0] = minLat;
		boundingBox[1] = minLong;
		boundingBox[2] = maxLat;
		boundingBox[3] = maxLong;

		return boundingBox;
	}

	private class RouteListTask extends AsyncTask<String, Void, String> {
		ProgressDialog progress;

		double latitudeOrigem, longitudeOrigem;
		double latitudeDestino, longitudeDestino;

		double[]  pontosOrigem, pontosDestino;
		PerimetroApp perimetroOrigem, perimetroDestino;

		//Lista com os 2 objetos de perimetros que iremos montar
		List<PerimetroApp> perimetros;

		//ArrayList que vai receber a lista de rotas do WS
		ArrayList<RotaApp> rotas;

		//Construtor da task, que recebe a lat/long de origem e destino
		public RouteListTask(double latitudeOrigem, double longitudeOrigem, double latitudeDestino, double longitudeDestino){
			this.latitudeOrigem = latitudeOrigem;
			this.longitudeOrigem = longitudeOrigem;
			this.latitudeDestino = latitudeDestino;
			this.longitudeDestino = longitudeDestino;
		}

		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, context, "Buscando listas", "Aguarde...");
			perimetros = new ArrayList<PerimetroApp>();

			//Recebe os 4 pontos base para criar o perimetro (defini 1000 e 2000 depois temos que ver como vai ficar)
			pontosOrigem = getBoundingBox(latitudeOrigem, longitudeOrigem, 500);
			pontosDestino = getBoundingBox(latitudeDestino, longitudeDestino, 1000);

			//Instancia o objeto perimetro com os dados certinhos, que foram retornados acima.
			perimetroOrigem = new PerimetroApp(pontosOrigem[2], pontosOrigem[0], pontosOrigem[1], pontosOrigem[3]); 
			perimetroDestino = new PerimetroApp(pontosDestino[2], pontosDestino[0], pontosDestino[1], pontosDestino[3]);

			//Adicionamos na lista de permitros os dois itens que s�o usados no WS posteriormente
			perimetros.add(perimetroOrigem);
			perimetros.add(perimetroDestino);
		}

		@Override
		protected String doInBackground(String... urls) {

			String response = "";

			WSTaxiShare ws = new WSTaxiShare();
			try {
				//Aqui passamos a lista de perimetros para o WS fazer a busca e retornar a lista de rotas
				rotas = ws.getRotasPerimetro(perimetros);
				response = "{errorCode:0, descricao:Sucesso}";

			} catch (Exception e) {
				response = "{errorCode:1, descricao:Erro ao carregar rotas!}";
				Utils.logException("SearchRoteFragment", "RouteListTask", "doInBackground", e);
			}

			return response;
		}

		@Override
		protected void onPostExecute(String response) {

			try {
				JSONObject jsonResposta = new JSONObject(response);
				
				//Retira do rotas buscadas as rotas com numero de passageiros >= 4
				for (int i = 0; i < rotas.size(); i++){
					if (rotas.get(i).getPassExistentes() >= 4){
						rotas.remove(i);

					}
				}

				//Checamos se a resposta teve sucesso e se retornou uma lista de rotas
				if(jsonResposta.getInt("errorCode")==0 && rotas.size() > 0){
					//Caso tenha retornado, passamos a lista para o proximo fragment
					Bundle args = new Bundle();
					args.putParcelable("destinoAddress", dest);
					args.putParcelable("origemAddress", ori);
					args.putParcelableArrayList("rotas",rotas);								
					Utils.changeFragment(getFragmentManager(), new ListRoteFragment(), args);
				}
				else{
					//Caso contrario, informaos que nenhuma rota foi encontrada.
					//Utils.gerarToast(context, "Nenhuma rota encontrada!");
					questionaCriaRota();


				}
			} catch (Exception e) {
				Utils.logException("SearchRoteFragment", "RouteListTask", "onPostExecute", e);
			}

			//			ISSO AQUI � O TESTE QUE MARCA OS PONTOS NO MAPA, VOU DEIXAR PARA TESTARMOS QUALQUER COISA.
			//						LatLng latlng1 = new LatLng(perimetroOrigem.getCima(), perimetroOrigem.getEsquerda());
			//						LatLng latlng2 = new LatLng(perimetroOrigem.getCima(), perimetroOrigem.getDireita());
			//						LatLng latlng3 = new LatLng(perimetroOrigem.getBaixo(), perimetroOrigem.getEsquerda());
			//						LatLng latlng4 = new LatLng(perimetroOrigem.getBaixo(), perimetroOrigem.getDireita());
			//						
			//						LatLng latlng10 = new LatLng(perimetroDestino.getCima(), perimetroDestino.getEsquerda());
			//						LatLng latlng20 = new LatLng(perimetroDestino.getCima(), perimetroDestino.getDireita());
			//						LatLng latlng30 = new LatLng(perimetroDestino.getBaixo(), perimetroDestino.getEsquerda());
			//						LatLng latlng40 = new LatLng(perimetroDestino.getBaixo(), perimetroDestino.getDireita());
			//						
			//						
			//						googleMap.addMarker(new MarkerOptions().position(latlng1).title("Origem 1"));
			//						googleMap.addMarker(new MarkerOptions().position(latlng2).title("Origem 2"));
			//						googleMap.addMarker(new MarkerOptions().position(latlng3).title("Origem 3"));
			//						googleMap.addMarker(new MarkerOptions().position(latlng4).title("Origem 4"));
			//						
			//						googleMap.addMarker(new MarkerOptions().position(latlng10).title("Destino 1"));
			//						googleMap.addMarker(new MarkerOptions().position(latlng20).title("Destino 2"));
			//						googleMap.addMarker(new MarkerOptions().position(latlng30).title("Destino 3"));
			//						googleMap.addMarker(new MarkerOptions().position(latlng40).title("Destino 4"));

			progress.dismiss();
		}		
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
		googleMap = mapView.getMap();
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		setMarker();
		centerMapOnMyLocation();
		
	
		
	}
	

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		
		
	}
	

	@Override
	public void onDestroy() {
		mapView.onDestroy();
		
		super.onDestroy();
	}
	


	public void questionaCriaRota(){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle("Rota n�o encontrada");

		// set dialog message
		alertDialogBuilder
		.setMessage("N�o foram encontradas rotas, deseja criar uma com os endere�os da busca?")
		.setCancelable(false)
		.setPositiveButton("Criar",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				Bundle args = new Bundle();
				//Leva os 2 Address para a fragment de cria��o de rotas
				args.putParcelable("origemAddress", ori);
				args.putParcelable("destinoAddress", dest);
				Utils.changeFragment(getFragmentManager(),  new CreateRoteFragment(), args);

			}
		})
		.setNegativeButton("N�o",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}



	private void centerMapOnMyLocation() {
		
		if(gps.canGetLocation()){

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			LatLng myLocation = null;
			Geocoder g = new Geocoder(context, Locale.getDefault());

			try {
				List<Address> addresses = g.getFromLocation(latitude, longitude, 1);
				if(addresses.size() > 0){
					txtEndereco1.setText(addresses.get(0).getAddressLine(0));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myLocation = new LatLng(latitude,
					longitude);
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
					16));
		}	  else {
			gps.showSettingsAlert();
		}

	}

	public void onBackPressed() {

		if (doubleBackToExitPressedOnce) {
			onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		onResume();

	} 

	 private void verificaTamanhoTela(){

		 if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {     
			 txtEndereco1.setTextSize(14);
			 txtEndereco1.getLayoutParams().width = 350;
			 txtEndereco2.setTextSize(14);
			 txtEndereco2.getLayoutParams().width = 350;
		 }

	 }
}




