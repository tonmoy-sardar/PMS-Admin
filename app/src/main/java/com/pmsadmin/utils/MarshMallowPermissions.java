package com.pmsadmin.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.pmsadmin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hari Narayan Jha on 4/26/2016.
 */
public class MarshMallowPermissions {

    public static final int AUDIO_RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    public static final int FINE_LOCATION_PERMISSION_REQUEST_CODE = 4;
    public static final int COARSE_LOCATION_PERMISSION_REQUEST_CODE = 5;
    public static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 6;
    public static final int WRITE_TO_CALENDAR_PERMISSION_REQUEST_CODE = 7;
    public static final int RECORD_MEDIA_PERMISSION_REQUEST_CODE = 8;
    public static final int FINGERPRINT_PERMISSION_REQUEST_CODE = 9;

    //
    public static final int CAMERA_AT_ONCE_REQUEST_CODE = 101;
    public static final int GPS_AT_ONCE_REQUEST_CODE = 102;
    public static final int BCOS_SPLASH_AT_ONCE_REQUEST_CODE = 103;

    Activity activity;

    public MarshMallowPermissions(Activity activity) {
        this.activity = activity;
    }

    private boolean isBelowMarshmallow() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        return false;
    }

    /**
     * **********************************************************************************************************************
     * *********************************** CHECK PERMISSION SECTION ==> STARTS **********************************************
     * **********************************************************************************************************************
     */

    /**
     * WRITE TO CALENDAR PERMISSION CHECK
     *
     * @return
     */
    public boolean checkPermissionForWriteToCalendar() {
        if (isBelowMarshmallow())
            return true;

        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * WRITE TO CALENDAR PERMISSION REQUEST
     */
    public void requestPermissionForWriteToCalendar() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_CALENDAR)) {
            showMessageOKCancel(activity.getResources().getString(R.string.write_to_calendar),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR}, WRITE_TO_CALENDAR_PERMISSION_REQUEST_CODE);
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR}, WRITE_TO_CALENDAR_PERMISSION_REQUEST_CODE);
        }
    }

    public boolean checkPermissionForFingerPrint() {
        if (isBelowMarshmallow())
            return true;

        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * FINGERPRINT PERMISSION REQUEST
     */

    public void requestPermissionForFingerPrint() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.USE_FINGERPRINT)) {
            showMessageOKCancel(activity.getResources().getString(R.string.finger_print),

                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.USE_FINGERPRINT}, FINGERPRINT_PERMISSION_REQUEST_CODE);
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.USE_FINGERPRINT}, FINGERPRINT_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * READ CONTACTS PERMISSION CHECK
     *
     * @return
     */
    public boolean checkPermissionForReadContacts() {
        if (isBelowMarshmallow())
            return true;

        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * READ CONTACTS PERMISSION REQUEST
     */
    public void requestPermissionForReadContacts() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
            showMessageOKCancel(activity.getResources().getString(R.string.read_contacts),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSION_REQUEST_CODE);
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * AUDIO RECORD PERMISSION CHECK
     *
     * @return
     */
    public boolean checkPermissionForAudioRecord() {
        if (isBelowMarshmallow())
            return true;

        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    //

    /**
     * AUDIO RECORD PERMISSION REQUEST
     */
    public void requestPermissionForAudioRecord() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
            showMessageOKCancel(activity.getString(R.string.record_audio),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO},
                                        AUDIO_RECORD_PERMISSION_REQUEST_CODE);
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO},
                    AUDIO_RECORD_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * FINE LOCATION PERMISSION CHECK
     *
     * @return
     */
    public boolean checkPermissionForFineLocation() {
        if (isBelowMarshmallow())
            return true;

        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    //

    /**
     * FINE LOCATION PERMISSION REQUEST
     */
    public void requestPermissionForFineLocation() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showMessageOKCancel(activity.getString(R.string.access_fine_location),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(activity, new String[]
                                        {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION_REQUEST_CODE);

                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * COARSE LOCATION PERMISSION CHECK
     *
     * @return
     */
    public boolean checkPermissionForCoarseLocation() {
        if (isBelowMarshmallow())
            return true;

        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    //

    /**
     * COARSE LOCATION PERMISSION REQUEST
     */
    public void requestPermissionForCoarseLocation() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            showMessageOKCancel(activity.getString(R.string.access_coarse_location),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_PERMISSION_REQUEST_CODE);
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * WRITE EXTERNAL STORAGE PERMISSION CHECK
     *
     * @return
     */
    public boolean checkPermissionForWriteExternalStorage() {
        if (isBelowMarshmallow())
            return true;

        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    //

    /**
     * WRITE EXTERNAL STORAGE PERMISSION REQUEST
     */
    public void requestPermissionForExternalStorage() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showMessageOKCancel(activity.getString(R.string.write_external_storage),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * CAMERA PERMISSION CHECK
     *
     * @return
     */
    public boolean checkPermissionForCamera() {
        if (isBelowMarshmallow())
            return true;

        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    //

    /**
     * CAMERA PERMISSION REQUEST
     */
    public void requestPermissionForCamera() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            showMessageOKCancel(activity.getString(R.string.camera),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * **********************************************************************************************************************
     * *********************************** CHECK SINGLE PERMISSION SECTION ==> ENDS ******************************************
     * **********************************************************************************************************************
     */


    /**
     * **********************************************************************************************************************
     * *********************************** GPS PERMISSION AT ONCE ==> STARTS **********************************************
     * **********************************************************************************************************************
     */

    /**
     * false ==> Some permissions not allowed
     * true ==> All permissions allowed
     *
     * @return
     */

    public boolean isAllGpsPermissionAllowed() {
        if (isBelowMarshmallow())
            return true;

        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add(activity.getString(R.string.tag_access_fine_location));
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add(activity.getString(R.string.tag_access_coarse_location));

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = activity.getString(R.string.cannot_proceed_permission_msg) + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                                            GPS_AT_ONCE_REQUEST_CODE);
                                }
                            }
                        });
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                        GPS_AT_ONCE_REQUEST_CODE);
            }
            return false;
        }

        return true;
    }
    /**
     * **********************************************************************************************************************
     * *********************************** GPS PERMISSION AT ONCE ==> ENDS **********************************************
     * **********************************************************************************************************************
     */


    /**
     * **********************************************************************************************************************
     * *********************************** CAMERA PERMISSION AT ONCE ==> STARTS **********************************************
     * **********************************************************************************************************************
     */

    /**
     * false ==> Some permissions not allowed
     * true ==> All permissions allowed
     *
     * @return
     */

    public boolean isAllCameraPermissionAllowed() {
        if (isBelowMarshmallow())
            return true;

        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add(activity.getString(R.string.tag_camera));
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add(activity.getString(R.string.tag_record_audio));
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add(activity.getString(R.string.tag_write_external_storage));

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = activity.getString(R.string.cannot_proceed_permission_msg) + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                                            CAMERA_AT_ONCE_REQUEST_CODE);
                                }
                            }
                        });
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                        CAMERA_AT_ONCE_REQUEST_CODE);
            }
            return false;
        }

        return true;
    }
    /**
     * **********************************************************************************************************************
     * *********************************** CAMERA PERMISSION AT ONCE ==> ENDS **********************************************
     * **********************************************************************************************************************
     */


    /**
     * **********************************************************************************************************************
     * ******************** WRITE STORAGE,
     * ******************** WRITE TO CALENDAR PERMISSION AT ONCE
     * ******************** ================== STARTS ===============
     * **********************************************************************************************************************
     */

    /**
     * false ==> Some permissions not allowed
     * true ==> All permissions allowed
     *
     * @return
     */

    public boolean isAllPlayfiksSplashPermissionAllowed() {
        if (isBelowMarshmallow())
            return true;

        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        /*if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add(activity.getString(R.string.tag_write_external_storage));
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add(activity.getString(R.string.tag_read_external_storage));
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add(activity.getString(R.string.record_audio));
        if (!addPermission(permissionsList, Manifest.permission.USE_FINGERPRINT))
            permissionsNeeded.add(activity.getString(R.string.finger_print));*/
        /*if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add(activity.getString(R.string.read_contacts));*/
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add(activity.getString(R.string.access_fine_location));

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = activity.getString(R.string.cannot_proceed_permission_msg) + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                                            BCOS_SPLASH_AT_ONCE_REQUEST_CODE);
                                }
                            }
                        });
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                        BCOS_SPLASH_AT_ONCE_REQUEST_CODE);
            }
            return false;
        }

        return true;
    }

    /**
     * **********************************************************************************************************************
     * ******************** WRITE STORAGE,
     * ******************** WRITE TO CALENDAR PERMISSION AT ONCE
     * ******************** ================== ENDS ===============
     * **********************************************************************************************************************
     */


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                    return false;
            }
        }
        return true;
    }


    public void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {

        try {
            new AlertDialog.Builder(activity)
                    .setCancelable(false)
                    .setMessage(message)
                    .setPositiveButton(activity.getString(R.string.permission_dialog_button_ok), okListener)
                    //.setNegativeButton(activity.getString(R.string.permission_dialog_button_cancel), null)
                    .create()
                    .show();
        } catch (Exception e) {
        }
    }
}

