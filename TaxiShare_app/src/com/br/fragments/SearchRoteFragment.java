package com.br.fragments;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.activitys.R;
import com.br.resources.Utils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.br.resources.Utils;


public class SearchRoteFragment extends Fragment implements OnClickListener{

	// Google Map
	private GoogleMap googleMap;
	private static View view;
	private MapView m;
	private Bundle mBundle;
	private Button btBusca;
	private EditText txtEndereco1, txtEndereco2;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.teste_mapa, container, false);

		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO handle this situation
		}

		m = (MapView) view.findViewById(R.id.map);
		m.onCreate(mBundle);
		setUpMapIfNeeded(view);
		criaTela();



		return view;	


	}

	@Override
	public void onResume() {
		super.onResume();
		m.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		m.onPause();
	}

	@Override
	public void onDestroy() {
		m.onDestroy();
		super.onDestroy();
	}



	private void setUpMapIfNeeded(View inflatedView) {
		if (googleMap == null) {
			googleMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
			if (googleMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		googleMap.addMarker(new MarkerOptions().position(new LatLng(-23.489839, -46.410520)).title("Marker"));
		setaZoom();
	}



	public void setaZoom(){
		//Location myLocation = googleMap.getMyLocation();
		LatLng myLatLng = new LatLng(-23.489839,
				-46.410520);

		//Adiciona a latitude e longitude da minha localiza��o a um objeto LatLng


		//Move a camera do mapa para a minha localiza��o de acordo com o objeto LatLng gerado
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

	}



	public String procuraEndereco(String endereco, int respostas) throws IOException{

		// esse Geocoder aqui � quem vai traduzir o endere�o de String para coordenadas double
		Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());

		//este Adress aqui recebe um retorno do metodo geoCoder.getFromLocationName vc manipula este retorno pra pega as coordenadas
		List<Address> enderecos = null;  

		// o numero um aqui � a quantidade maxima de resultados que vc quer receber
		enderecos = geoCoder.getFromLocationName(endereco, respostas);

		Address address = enderecos.get(0);

		String	end = address.getAddressLine(0) + ", 10, " + address.getSubLocality() + ", " + address.getLocality() + ", Sao Paulo";

		return end;

		//Utils.gerarToast(getActivity(), end);
	}

	public void criaTela(){

		btBusca = (Button) view.findViewById(R.id.btBusca);
		txtEndereco1 = (EditText) view.findViewById(R.id.txtEndereco1);
		txtEndereco2 = (EditText) view.findViewById(R.id.txtEndereco2);

		btBusca.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				context = view.getContext();
				String origem = txtEndereco1.getText().toString();
				String destino = txtEndereco2.getText().toString();
				try {
					origem = procuraEndereco(origem, 1);
					destino = procuraEndereco(destino, 10);
				} catch (IOException e) {
					e.printStackTrace();
					Utils.gerarToast(context, "Nenhum Endere�o Encontrado");
				}

				Bundle args = new Bundle();
				args.putCharSequence("origem", origem);
				args.putCharSequence("destino", destino);
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ftransaction = fragmentManager.beginTransaction();
				Fragment fragment = new ListRoteFragment();
				fragment.setArguments(args);
				ftransaction.replace(R.id.content_frame, fragment);
				ftransaction.addToBackStack(null);
				ftransaction.commit();
			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btBusca) {
			String e = txtEndereco1.getText().toString();
			try {
				procuraEndereco(e, 1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}


	}

	/**

	else if (id == R.id.btEnderecos) {

		Intent intent = new Intent (TaxyShareMapa.this, MinhaRota.class);
		Bundle extras = new Bundle();

		startActivity(intent);


	}

	 **/
}
