# CameraVision
Visual recognition algorithms for various streaming cameras.

The "cameras" and "computervision" packages are proposed structure for handling cameras and all vision related tasks including QR code scanning and deep learning based classification.  Kinect, webcam, and deep learning parts are working, will be tested further, and should be ready for integration into the "Greggg" project rather immediately after group review.

The only changes to the Camera code is to implement the observer pattern, which works with both webcams and kinect.

The project shows by example my earlier proposed change to how the main UI is spawned and how objects may relate to it.

If some functionality is superfluous to the robot, then it may stand alone within this project, and at that point this project will remain a testbed for future computer vision experiments.
