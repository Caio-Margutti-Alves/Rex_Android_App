package com.main.rexx;

import user.User;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import camera.CameraActivity;

public class RegisterAccountActivity extends CameraActivity {

	private RegisterUserTask registerTask = null;

	private static String firstName;
	private static String lastName;
	private static String login;
	private static String password;
	private static String rePassword;
	private static String mobileToken;
	//private static Bitmap profilePicture;
	private static int gender;
	private static String email;
	private static String dob;

	private Button btnMale;
	private Button btnFemale;
	private EditText edtLogin;
	private EditText edtPassword;
	private EditText edtFirstName;
	private EditText edtLastName;
	private EditText edtEmail;
	private EditText edtRePassword;

	private View loginFormView;
	private View loginStatusView;
	private Button btnRegister;

	private CameraActivity cameraActivity;
	private Button btnTakePhoto;
	private ImageView imgvRegisterProfile;
	private static final int ACTION_TAKE_PHOTO = 1;

	Button.OnClickListener btnPhotoOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
		}
	};

	Button.OnClickListener lstnMale = new Button.OnClickListener() {
		@Override
		public void onClick(View view) {
			view.setBackgroundColor(Color.rgb(177,235,000));
			btnFemale.setBackgroundColor(Color.rgb(243,243,243));
			gender = 1;
		}
	};
	
	Button.OnClickListener lstnFemale = new Button.OnClickListener() {
		@Override
		public void onClick(View view) {
			view.setBackgroundColor(Color.rgb(177,235,000));
			btnMale.setBackgroundColor(Color.rgb(243,243,243));
			gender = 2;
			}
	};
	
	Button.OnClickListener lstnRegister = new Button.OnClickListener() {
		@Override
		public void onClick(View view) {
			attemptRegister();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_account);

		btnMale = (Button) findViewById(R.id.btnMale);
		btnFemale = (Button) findViewById(R.id.btnFemale);
		btnMale.setOnClickListener(lstnMale);
		btnFemale.setOnClickListener(lstnFemale);
		edtLogin = (EditText) findViewById(R.id.login);
		edtPassword = (EditText) findViewById(R.id.password);
		edtFirstName = (EditText) findViewById(R.id.firstName);
		edtLastName = (EditText) findViewById(R.id.lastName);
		edtEmail = (EditText) findViewById(R.id.email);
		edtRePassword = (EditText) findViewById(R.id.rePassword);
		
		loginFormView = findViewById(R.id.login_form);
		loginStatusView = findViewById(R.id.login_status);

		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(lstnRegister);

		btnTakePhoto = (Button) findViewById(R.id.btnTakePicture);
		imgvRegisterProfile = (ImageView) findViewById(R.id.imgvRegisterProfile);
		super.setBtnListenerOrDisable(btnTakePhoto, btnPhotoOnClickListener,
				MediaStore.ACTION_IMAGE_CAPTURE);
		cameraActivity = new CameraActivity(imgvRegisterProfile);

		btnMale.setBackgroundColor(Color.rgb(177,235,000));
		gender = 1;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			// openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			loginStatusView.setVisibility(View.VISIBLE);
			loginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							loginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			loginFormView.setVisibility(View.VISIBLE);
			loginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							loginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	public void attemptRegister() {
		if (registerTask != null) {
			return;
		}

		// Reset errors.
		edtFirstName.setError(null);
		edtLastName.setError(null);
		edtLogin.setError(null);
		edtPassword.setError(null);
		edtEmail.setError(null);
		edtRePassword.setError(null);

		// Store values at the time of the login attempt.
		firstName = edtFirstName.getText().toString();
		lastName = edtLastName.getText().toString();
		email = edtEmail.getText().toString();
		login = edtLogin.getText().toString();
		password = edtPassword.getText().toString();
		rePassword = edtRePassword.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			edtPassword.setError(getString(R.string.error_field_required));
			focusView = edtPassword;
			cancel = true;
		} else if (password.length() < 8) {
			edtPassword.setError(getString(R.string.error_invalid_password));
			focusView = edtPassword;
			cancel = true;
		}

		// Check for a valid rePassword.
		if (TextUtils.isEmpty(rePassword)) {
			edtRePassword.setError(getString(R.string.error_field_required));
			focusView = edtRePassword;
			cancel = true;
		} else if (!password.equals(rePassword)) {
			edtRePassword.setError(getString(R.string.error_invalid_repassword));
			focusView = edtRePassword;
			cancel = true;
		}

		// Check for a valid login.
		if (TextUtils.isEmpty(login)) {
			edtLogin.setError(getString(R.string.error_field_required));
			focusView = edtLogin;
			cancel = true;
		} else if (login.length() < 8) {
			edtLogin.setError(getString(R.string.error_invalid_login));
			focusView = edtLogin;
			cancel = true;
		}
		
		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			edtEmail.setError(getString(R.string.error_field_required));
			focusView = edtEmail;
			cancel = true;
		} else if (!email.contains("@")) {
			edtEmail.setError(getString(R.string.error_invalid_email));
			focusView = edtEmail;
			cancel = true;
		}
		
		// Check for a valid first and last name.
		if (TextUtils.isEmpty(lastName)) {
			edtLastName.setError(getString(R.string.error_field_required));
			focusView = edtLastName;
			cancel = true;
		} 
		
		if (TextUtils.isEmpty(firstName)) {
			edtFirstName.setError(getString(R.string.error_field_required));
			focusView = edtFirstName;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			registerTask = new RegisterUserTask();
			registerTask.execute((Void) null);
		}
	}

	public class RegisterUserTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				User.newUser(null, login, password, mobileToken, firstName, lastName,
						email, String.valueOf(gender), dob);
			} catch (Exception e) {
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			registerTask = null;
			showProgress(false);

			if (success) {
				/*Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
				startActivity(intent);*/
				finish();
			} else {
				
				//TODO
				edtPassword.setText("");
				edtPassword.setError(getString(R.string.error_incorrect_combination));
				edtPassword.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			registerTask = null;
			showProgress(false);
		}
	}
}
