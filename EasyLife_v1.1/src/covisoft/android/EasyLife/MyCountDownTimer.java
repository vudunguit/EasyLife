package covisoft.android.EasyLife;

import android.os.CountDownTimer;

public class MyCountDownTimer extends CountDownTimer{

	int stop = 10*1000;
	
	public MyCountDownTimer(int duration, int next) {
		super(duration, next);
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTick(long millisUntilFinished) {
		// TODO Auto-generated method stub
		
	}

	public int getStop() {
		return stop;
	}

	public void setStop(int stop) {
		this.stop = stop;
	}
	
}
