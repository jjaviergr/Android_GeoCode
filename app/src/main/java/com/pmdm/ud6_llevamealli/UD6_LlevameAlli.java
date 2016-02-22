package com.pmdm.ud6_llevamealli;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class UD6_LlevameAlli extends Activity {

	private CheckBox cbDireccion;
	private EditText etDireccion;
	private TextView tvInformacion;

	private Address objetoDireccion = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ud6_llevame_alli);

		cbDireccion = (CheckBox) findViewById(R.id.cbDireccion);
		etDireccion = (EditText) findViewById(R.id.etDireccion);
		tvInformacion = (TextView) findViewById(R.id.tvInformacion);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_ud6_llevame_alli, menu);
		return true;
	}

	private Address convertirGeocode(String direccion, boolean esDireccion) {
		// lista de direcciones obtenidas por Geocoder
		List<Address> direcciones = null;
		// Construimos el objeto Geocoder
		Geocoder geocoder = new Geocoder(this);
		// si es una dirección postal (o sea, si el usuario marcó la casilla)
		if (esDireccion) {
			try {
				// obtenemos el array de direcciones por el nombre, indicándole
				// que no se quede más que con una
				direcciones = geocoder.getFromLocationName(direccion, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// si no es una dirección postal (lo que nos indica el propio usuario
		// cuando no marca la casilla)
		else {
			// y supuesto que la cadena introducida por el usuario tiene la
			// forma correcta (por ejemplo, "36.840326,-2.459256"), empezamos
			// por romper la cadena por la ','
			String[] coordenadas = direccion.split(",");
			// si hemos obtenido dos partes (como se espera)
			if (coordenadas != null && coordenadas.length == 2) {
				try {
					// convertimos cada parte a Double antes de pasarlas
					// al método que obtiene el array de direcciones por las
					// coordenadas, indicándole que sólo se quede con una
					direcciones = geocoder.getFromLocation(
							Double.parseDouble(coordenadas[0]),
							Double.parseDouble(coordenadas[1]), 1);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// devolvemos la dirección obtenida (o sea, el primer y único elemento
		// del array)
		if (direcciones != null && direcciones.size() > 0)
			return direcciones.get(0);
		else
			return null;
	}

	
	
	public void onClickGeocode(View view) {
		// obtiene los datos del usuario
		boolean esDireccion = cbDireccion.isChecked();
		String direccion = etDireccion.getText().toString();
		// asigna el objeto con la información de la ubicación
		objetoDireccion = convertirGeocode(direccion, esDireccion);
		if (objetoDireccion != null)
			// muestra la información a lo bruto (si se desea, cada parte
			// de ella se puede extraer mediante alguno de los muchos métodos
			// get disponibles, como .getCountryName(), .getPostalCode(), etc.)
			tvInformacion.setText(objetoDireccion.toString());
		else
			tvInformacion.setText(null);
	}
	
	public void onClickLlevame(View view) {
		// si está asignado el objeto dirección
		if (objetoDireccion != null) {
			// obtiene la latitud y la longitud
			double latitud = objetoDireccion.getLatitude();
			double longitud = objetoDireccion.getLongitude();

			// muestra el lugar en el mapa
			Uri uri = Uri.parse("geo:" + latitud + "," + longitud);
			Intent i = new Intent();
			i.setAction(Intent.ACTION_VIEW);
			i.setData(uri);
			
			startActivity(i);
		}
	}

}
