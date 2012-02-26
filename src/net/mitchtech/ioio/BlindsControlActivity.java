package net.mitchtech.ioio;

import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;
import net.mitchtech.ioio.blindscontrol.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BlindsControlActivity extends AbstractIOIOActivity {
	private static final int BLINDS_PIN1 = 3;
	private static final int BLINDS_PIN2 = 6;

	private static final int PWM_FREQ = 100;

	private static final int PWM_FWD = 999;
	private static final int PWM_REV = 1;
	private static final int PWM_OFF = 500;

	private Button mBlinds1Rev;
	private Button mBlinds1Off;
	private Button mBlinds1Fwd;

	private Button mBlinds2Rev;
	private Button mBlinds2Off;
	private Button mBlinds2Fwd;

	private int mBlinds1State = PWM_OFF;
	private int mBlinds2State = PWM_OFF;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mBlinds1Rev = (Button) findViewById(R.id.blinds1rev);
		mBlinds1Rev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBlinds1State = PWM_REV;
			}
		});

		mBlinds1Off = (Button) findViewById(R.id.blinds1off);
		mBlinds1Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBlinds1State = PWM_OFF;
			}
		});

		mBlinds1Fwd = (Button) findViewById(R.id.blinds1fwd);
		mBlinds1Fwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBlinds1State = PWM_FWD;
			}
		});

		mBlinds2Rev = (Button) findViewById(R.id.blinds2rev);
		mBlinds2Rev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBlinds2State = PWM_REV;
			}
		});

		mBlinds2Off = (Button) findViewById(R.id.blinds2off);
		mBlinds2Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBlinds2State = PWM_OFF;
			}
		});

		mBlinds2Fwd = (Button) findViewById(R.id.blinds2fwd);
		mBlinds2Fwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBlinds2State = PWM_FWD;
			}
		});

		enableUi(false);
	}

	class IOIOThread extends AbstractIOIOActivity.IOIOThread {
		private PwmOutput mBlinds1;
		private PwmOutput mBlinds2;

		public void setup() throws ConnectionLostException {
			try {
				mBlinds1 = ioio_.openPwmOutput(BLINDS_PIN1, PWM_FREQ);
				mBlinds2 = ioio_.openPwmOutput(BLINDS_PIN2, PWM_FREQ);
				enableUi(true);
			} catch (ConnectionLostException e) {
				enableUi(false);
				throw e;
			}
		}

		public void loop() throws ConnectionLostException {
			try {
				mBlinds1.setPulseWidth(500 + mBlinds1State * 2);
				mBlinds2.setPulseWidth(500 + mBlinds2State * 2);
				sleep(10);
			} catch (InterruptedException e) {
				ioio_.disconnect();
			} catch (ConnectionLostException e) {
				enableUi(false);
				throw e;
			}
		}
	}

	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}

	private void enableUi(final boolean enable) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mBlinds1Rev.setEnabled(enable);
				mBlinds1Off.setEnabled(enable);
				mBlinds1Fwd.setEnabled(enable);
				mBlinds2Rev.setEnabled(enable);
				mBlinds2Off.setEnabled(enable);
				mBlinds2Fwd.setEnabled(enable);
			}
		});
	}
}