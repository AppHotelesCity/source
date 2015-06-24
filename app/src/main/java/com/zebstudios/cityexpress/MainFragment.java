package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import static com.appsee.Appsee.addEvent;

public class MainFragment extends Fragment {
	private final int FRAGMENT_LIST_STATES = 100;

	private View _view;
	private int _currentSelectedOption;
	private ArrayList < ImageOption > _options;
	private ArrayList < Hotel > _results;
	private State _state;
	private PromoCode _promocode;
	private ProgressDialogFragment _progress;
	private Hotel _tempHotel;

	private static final int RESULTS_ACTIVITY = 1000;
	private static final int DETAILS_ACTIVITY = 1001;

	public MainFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		_state = null;

		_view = inflater.inflate(R.layout.fragment_main, container, false);

		_currentSelectedOption = -1;
		_options = new ArrayList < ImageOption > ();

		ImageView imgBar1 = (ImageView) _view.findViewById(R.id.imgOptionBar1);
		_options.add(new ImageOption(imgBar1, R.drawable.bar_marca_1_on, R.drawable.bar_marca_1_off, "express"));
		ImageView imgBar2 = (ImageView) _view.findViewById(R.id.imgOptionBar2);
		_options.add(new ImageOption(imgBar2, R.drawable.bar_marca_2_on, R.drawable.bar_marca_2_off, "plus"));
		ImageView imgBar3 = (ImageView) _view.findViewById(R.id.imgOptionBar3);
		_options.add(new ImageOption(imgBar3, R.drawable.bar_marca_3_on, R.drawable.bar_marca_3_off, "suites"));
		ImageView imgBar4 = (ImageView) _view.findViewById(R.id.imgOptionBar4);
		_options.add(new ImageOption(imgBar4, R.drawable.bar_marca_4_on, R.drawable.bar_marca_4_off, "junior"));
		prepareOptions();

		ImageButton btnCities = (ImageButton) _view.findViewById(R.id.btnGetCities);
		btnCities.setOnClickListener(new View.OnClickListener() {@Override
																 public void onClick(View view) {
			getStates();
		}
		});

		TableRow searchRow = (TableRow) _view.findViewById(R.id.row_search);
		searchRow.setOnClickListener(new View.OnClickListener() {@Override
																 public void onClick(View view) {
			Search();
			addEvent("HomeSearch");
		}
		});





		return _view;
	}

	@Override
	public void onResume() {
		super.onResume();
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		activity.getSupportActionBar().setTitle("Principal");
	}

	private void Search() {
		EditText txtWord = (EditText) _view.findViewById(R.id.txtWord);
		String text = txtWord.getText().toString().trim();

		if (_currentSelectedOption == -1 && _state == null && text.length() == 0) {
			AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
			alert.setTitle("Atención");
			alert.setMessage("Por favor ingrese una palabra y/o seleccione una ciudad y/o una marca.");
			alert.setIcon(R.drawable.notification_warning_small);
			alert.setCancelable(false);
			alert.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			});
			alert.show();
		} else {
			new DoSearch().execute();
			Analytics analytics = (Analytics) getActivity().getApplication();
			if (_state != null) {
				analytics.sendAppEventTrack("MAIN ANDROID", "SEARCH", "ESTADO", _state.getNombre(), 1);
			}
			if (_currentSelectedOption != -1) {
				analytics.sendAppEventTrack("MAIN ANDROID", "SEARCH", "MARCA", _options.get(_currentSelectedOption).getOption(), 1);
			}
		}
	}

	private void Searched() {
		EditText txtWord = (EditText) _view.findViewById(R.id.txtWord);
		String text = txtWord.getText().toString().trim();

		android.util.Log.d("TEST", "RESULTS: " + _results.size());
		if (_results.size() == 0) {
			AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
			alert.setTitle("Atención");
			if (text.length() > 0 && _currentSelectedOption != -1) alert.setMessage("No se encontraron hoteles de esa marca con la palabra que ingresó.");
			else if (text.length() > 0) alert.setMessage("No se encontraron hoteles con la palabra que ingresó.");
			else if (_state != null && _currentSelectedOption != -1) alert.setMessage("No se encontraron hoteles de esa marca en la ciudad que seleccionó.");
			else if (_state != null) alert.setMessage("No se encontraron hoteles en la ciudad que seleccionó.");
			else alert.setMessage("No se encontraron hoteles de esa marca.");
			alert.setIcon(R.drawable.notification_warning_small);
			alert.setCancelable(false);
			alert.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			});
			alert.show();
		} else if (_results.size() > 1) {
			Collections.sort(_results, new Hotel.HotelComparator());
			Intent dialog = new Intent(getActivity(), SearchResultsActivity.class);

			if (_state != null) dialog.putExtra("STATE", _state.getNombre());
			else dialog.putExtra("STATE", "");

			if (_currentSelectedOption != -1) dialog.putExtra("MARCA", _options.get(_currentSelectedOption).getOption());
			else dialog.putExtra("MARCA", "");

			if (text.length() > 0) dialog.putExtra("WORD", text);
			else dialog.putExtra("WORD", "");

			dialog.putExtra("RESULTS", _results);
			startActivityForResult(dialog, RESULTS_ACTIVITY);
		} else {
			_tempHotel = _results.get(0);
			new GetHotelData(_tempHotel.getId()).execute();
		}
	}

	private void hotelObtained(int res, Hotel hotel) {
		Intent dialog = new Intent(getActivity(), HotelDetailsActivity.class);
		if (res == 0) dialog.putExtra("HOTEL", hotel);
		else dialog.putExtra("HOTEL", _tempHotel);
		startActivityForResult(dialog, DETAILS_ACTIVITY);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == RESULTS_ACTIVITY || requestCode == DETAILS_ACTIVITY) && resultCode == Activity.RESULT_OK) {
			long reservationId = data.getLongExtra("RESERVATION_ID", 0);
			MainActivity mainActivity = (MainActivity) getActivity();
			mainActivity.presentReservation(reservationId);
		}
	}

	private void handleSearchButton() {
		TableRow searchRow = (TableRow) _view.findViewById(R.id.row_search);
		if (_currentSelectedOption == -1 && _state == null) {
			searchRow.setBackgroundColor(getResources().getColor(R.color.main_search_disabled));
		} else {
			searchRow.setBackgroundColor(getResources().getColor(R.color.main_search));
		}
	}

	// Busqueda por estado
	private void getStates() {
		StatesFragment fragment = new StatesFragment();
		fragment.setTargetFragment(this, FRAGMENT_LIST_STATES);
		getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).addToBackStack(null).commit();
	}

	public void setSelectedState(State state) {
		TextView lblState = (TextView) _view.findViewById(R.id.lblCity);
		_state = state;
		if (_state.getId() == -1) {
			_state = null;
			lblState.setText("Buscar por ciudad");
		} else {
			lblState.setText(_state.getNombre());
		}

		handleSearchButton();
	}

	public void setSelectedPromocode(PromoCode promocode) {

		EditText lblpromocode = (EditText) _view.findViewById(R.id.txtPromocode);
		_promocode = promocode;

		lblpromocode.setText(promocode.getnumpromocode());

		getFragmentManager().popBackStack();


	}




	private void prepareOptions() {
		for (int i = 0; i < _options.size(); i++) {
			ImageOption op = _options.get(i);
			final int index = i;
			op.getImageView().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
				handleOption(index);
			}
			});
		}
	}

	private void handleOption(int option) {
		if (_currentSelectedOption == option) {
			_options.get(option).SetOff();
			_currentSelectedOption = -1;
		} else {
			if (_currentSelectedOption != -1) _options.get(_currentSelectedOption).SetOff();
			_options.get(option).SetOn();
			_currentSelectedOption = option;
		}

		handleSearchButton();
	}

	private class GetHotelData extends AsyncTask < Void, Void, Integer > {
		private int _id;
		private Hotel _hotel;

		public GetHotelData(int id) {
			_id = id;
			_hotel = null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			_progress = ProgressDialogFragment.newInstance();
			_progress.setCancelable(false);
			_progress.show(getFragmentManager(), "Dialog");
		}

		@Override
		protected Integer doInBackground(Void...arg0) {
			String url = APIAddress.HOTELS_API_MOBILE + "/GetHotel/" + _id;

			android.util.Log.d("", "URL: " + url);
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONObject raw = new JSONObject(jsonStr);
					JSONObject h = raw.getJSONObject("Hotele");
					Hotel hotel = new Hotel(h, 1);
					if (!hotel.isParsedOk()) {
						// Error
						return -3;
					}

					_hotel = hotel;
					return 0;
				} catch (Exception e) {
					android.util.Log.e("JSONParser", "Cant parse: " + e.getMessage());
					return -2;
				}
			} else android.util.Log.e("ServiceHandler", "Couldn't get any data");

			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			_progress.dismiss();

			hotelObtained(result, _hotel);
		}
	}

	private class DoSearch extends AsyncTask < Void, Void, Integer > {@Override
																	  protected void onPreExecute() {
		super.onPreExecute();

		_progress = ProgressDialogFragment.newInstance();
		_progress.setCancelable(false);
		_progress.show(getFragmentManager(), "Dialog");
	}


		//BUSCAR POR CIUDAD & MARCA
		@Override
		protected Integer doInBackground(Void...arg0) {
			_results = new ArrayList < Hotel > ();

			EditText txtWord = (EditText) _view.findViewById(R.id.txtWord);
			String text = txtWord.getText().toString().trim();

			String url = "";
			if (text.length() > 0 && _currentSelectedOption != -1) url = APIAddress.HOTELS_API_MOBILE + "/SearchHotels?term=" + urlEncode(text) + "&marca=" + _options.get(_currentSelectedOption).getOption();
			else if (text.trim().length() > 0) url = APIAddress.HOTELS_API_MOBILE + "/SearchHotels?term=" + urlEncode(text) + "&marca=";
			else if (_state != null && _currentSelectedOption != -1) url = APIAddress.HOTELS_API_MOBILE + "/GetHotelsByCityAndBrand?cityId=" + _state.getId() + "&brandName=" + _options.get(_currentSelectedOption).getOption();
			else if (_state != null) url = APIAddress.HOTELS_API_MOBILE + "/GetHotelsByCity/" + _state.getId();
			else if (_currentSelectedOption != -1) url = APIAddress.HOTELS_API_MOBILE + "/GetHotelsByBrand?brandName=" + _options.get(_currentSelectedOption).getOption();

			android.util.Log.d("", "URL: " + url);
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONArray results = new JSONArray(jsonStr);
					for (int i = 0; i < results.length(); i++) {
						JSONObject h = results.getJSONObject(i);
						Hotel hotel = new Hotel(h);
						if (!hotel.isParsedOk()) {
							// Error
							return -3;
						}
						_results.add(hotel);
					}

					return 0;
				} catch (Exception e) {
					android.util.Log.e("JSONParser", "Cant parse: " + e.getMessage());
					return -2;
				}
			} else android.util.Log.e("ServiceHandler", "Couldn't get any data");

			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			_progress.dismiss();

			if (result != 0) {
				AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
				alert.setTitle("Atención");
				alert.setMessage("No se ha podido realizar su búsqueda en este momento. Por favor intente nuevamente más tarde.");
				alert.setIcon(R.drawable.notification_warning_small);
				alert.setCancelable(false);
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {@Override
																											  public void onClick(DialogInterface dialog, int which) {}
				});
				alert.show();
			} else {
				Searched();
			}
		}

		private String urlEncode(String text) {
			String encoded = "";

			try {
				encoded = URLEncoder.encode(text, "UTF-8");
			} catch (Exception e) {
				android.util.Log.w("ENCODER", "Cant encode text: " + e.getMessage());
			}

			return encoded;
		}
	}

	private class ImageOption {
		private ImageView _imageView;
		private int _onResource;
		private int _offResource;
		private String _option;

		public ImageOption(ImageView imageView, int onResource, int offResource, String option) {
			_imageView = imageView;
			_onResource = onResource;
			_offResource = offResource;
			_option = option;
		}

		public void SetOn() {
			_imageView.setImageResource(_onResource);
		}

		public void SetOff() {
			_imageView.setImageResource(_offResource);
		}

		public ImageView getImageView() {
			return _imageView;
		}

		public int getOnResource() {
			return _onResource;
		}

		public int getOffResource() {
			return _offResource;
		}

		public String getOption() {
			return _option;
		}
	}
}