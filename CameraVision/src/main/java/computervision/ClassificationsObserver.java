package computervision;

import java.util.ArrayList;

public interface ClassificationsObserver {

	void updatePredictions(ArrayList<Classification> predictions);
}
