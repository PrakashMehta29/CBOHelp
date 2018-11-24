package com.example.pc24.cbohelp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.example.pc24.cbohelp.appPreferences.Shareclass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

/**
 * Created by pc24 on 06/12/2016.
 */

public class up_down_ftp {
    public boolean status = false;
    FTPClient mFtpClient;
    Shareclass shareclass;
    private AdapterCallback mAdapterCallback;
    String web_root_path;
    long file_size=0;
    float file_uploaded=0.0f;


    public up_down_ftp(){

    }

    private Boolean connnectingwithFTP(String ip, String userName, String pass,final Context context) {

        try {
            mFtpClient = new FTPClient();
            mFtpClient.connect(ip);
            mFtpClient.login(userName, pass);
            status=true;
            Log.e("isFTPConnected", String.valueOf(status));
            mFtpClient.setType(FTPClient.TYPE_BINARY);
            web_root_path=shareclass.getValue(context,"WEB_ROOT_PATH","/DEMO/MAILFILES/CBOACCOUNTS/");

            //mFtpClient.changeDirectory("//DEMO/upload/");
            mFtpClient.changeDirectory(web_root_path);

            return true;
        } catch (SocketException e) {
            e.printStackTrace();
            try {
                mAdapterCallback.upload_complete("ERROR@"+"FTP not Configured");
            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            try {
                mAdapterCallback.upload_complete("N");
            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                mAdapterCallback.upload_complete("N");
            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        } catch (FTPException e) {

            try {
                mFtpClient.createDirectory(web_root_path);
                mFtpClient.changeDirectory(web_root_path);
                return true;
            } catch (IOException | FTPException | FTPIllegalReplyException e1) {
                e1.printStackTrace();

                try {
                    mAdapterCallback. upload_complete("ERROR@"+web_root_path);
                } catch (ClassCastException e2) {
                    throw new ClassCastException("Activity must implement AdapterCallback.");
                }

            }

            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
            try {
                mAdapterCallback.upload_complete("N");
            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        }
        return false;
    }

    public boolean downloadSingleFile(final String remoteFilePath,final  File downloadFile,final Context context,final String folderName) {

        final Boolean[] result = {false};
        Runnable runnable = new Runnable() {
            public void run() {
                shareclass=new Shareclass();
                connnectingwithFTP(shareclass.getValue(context,"WEB_IP","220.158.164.114"), shareclass.getValue(context,"WEB_USER","CBO_DOMAIN_SERVER"), shareclass.getValue(context,"WEB_PWD","cbodomain@321"),context);
                File parentDir = downloadFile.getParentFile();
                if (!parentDir.exists())
                    parentDir.mkdir();
                try {

                    mFtpClient.download(remoteFilePath, downloadFile);
                    mFtpClient.disconnect(true);
                    result[0] =true;
                   /* if(shareclass.getValue(context,"Chat","InActive").equals("Active")){
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    mAdapterCallback = ((AdapterCallback) context);
                                    mAdapterCallback. updateAdaptor();
                                } catch (ClassCastException e) {
                                    throw new ClassCastException("Activity must implement AdapterCallback.");
                                }

                            }
                        });
                    }*/
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        };
        Thread mythread1 = new Thread(runnable);
        mythread1.start();
        return result[0];
    }


    public static interface AdapterCallback {
        void  upload_complete(String IsCompleted);
    }

    public void uploadFile( final File uploadFile,final Context context) {
        Runnable runnable = new Runnable() {

            public void run() {
                shareclass=new Shareclass();
                mAdapterCallback = ((AdapterCallback) context);
                //connnectingwithFTP("220.158.164.114", "CBO_DOMAIN_SERVER", "cbodomain@321");
                if(connnectingwithFTP(shareclass.getValue(context,"WEB_IP","220.158.164.114"), shareclass.getValue(context,"WEB_USER","CBO_DOMAIN_SERVER"), shareclass.getValue(context,"WEB_PWD","cbodomain@321"),context)) {
                    try {


                        // code here to compress images......

                        compressImage(uploadFile);

                        //============================================

                        file_size = uploadFile.length();
                        mFtpClient.upload(uploadFile, new MyTransferListener(context));
                        mFtpClient.disconnect(true);
                    } catch (Exception e) {
                        try {
                            mAdapterCallback.upload_complete("N");
                        } catch (ClassCastException e1) {
                            throw new ClassCastException("Activity must implement AdapterCallback.");
                        }
                        e.printStackTrace();
                    }
                }
            }
        };
        final Thread mythread1 = new Thread(runnable);
        mythread1.start();

    }


    /*******  Used to file upload and show progress  **********/

    public class MyTransferListener implements FTPDataTransferListener {

        Context context;

        MyTransferListener(Context context){
            this.context=context;
        }

        public void started() {


            // Transfer started
            //Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" Upload Started ...");
            try {
                mAdapterCallback. upload_complete("S");
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
            //build notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_upload_file)
                            .setContentTitle("Upload Started ...")
                            .setContentText("Please Do not switch off your Internet")
                            .setAutoCancel(false)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setOngoing(true);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = 0x125688;
                mBuilder.setColor(color);
                mBuilder.setSmallIcon(R.drawable.ic_upload_file);
            }

            Random random = new Random();
            //int m = random.nextInt(9999 - 1000) + 1000;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build()); //m = ID of notification

        }

        public void transferred(int length) {

            // Yet other length bytes has been transferred since the last time this
            // method was called
            //Toast.makeText(getBaseContext(), " transferred ..." +MyConnection.user_name+ length, Toast.LENGTH_SHORT).show();
            //System.out.println(" transferred ..." + length);

            //build notification
            file_uploaded=file_uploaded +  length;
            float uploaded=file_uploaded*100/file_size;
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_upload_file)
                            .setContentTitle(""+uploaded+" % completed...")
                            .setContentText("Please Do not switch off your Internet ")
                            .setAutoCancel(false)
                            .setOngoing(true);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = 0x125688;
                mBuilder.setColor(color);
                mBuilder.setSmallIcon(R.drawable.ic_upload_file);
            }

            Random random = new Random();
            //int m = random.nextInt(9999 - 1000) + 1000;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build()); //m = ID of notification
        }

        public void completed() {


            // Transfer completed

            //Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" completed ..." );
            try {
                mAdapterCallback. upload_complete("Y");
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
            //build notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_upload_complete)
                            .setContentTitle("Upload completed")
                            .setContentText("Sucessfully uploaded...")
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = 0x125688;
                mBuilder.setColor(color);
                mBuilder.setSmallIcon(R.drawable.ic_upload_complete);
            }

            Random random = new Random();
            //int m = random.nextInt(9999 - 1000) + 1000;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build()); //m = ID of notification
        }

        public void aborted() {


            // Transfer aborted
            //Toast.makeText(getBaseContext()," transfer aborted , please try again...", Toast.LENGTH_SHORT).show();
            //System.out.println(" aborted ..." );
            try {
                mAdapterCallback. upload_complete("N");
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        }

        public void failed() {

            // Transfer failed
            System.out.println(" failed ..." );
            try {
                mAdapterCallback. upload_complete("N");
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
            //build notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_upload_failed)
                            .setContentTitle("Upload Failed ...")
                            .setContentText("Please try again...")
                            .setAutoCancel(false)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setOngoing(true);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = 0x125688;
                mBuilder.setColor(color);
                mBuilder.setSmallIcon(R.drawable.ic_upload_failed);
            }

            Random random = new Random();
            //int m = random.nextInt(9999 - 1000) + 1000;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build()); //m = ID of notification
        }

    }



    public String compressImage(File imageUri) {

        //String filePath = getRealPathFromURI(imageUri);
        String filePath=imageUri.getPath();
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        if (options.outWidth != -1 && options.outHeight != -1) {
            // This is an image file.

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

//      setting inSampleSize value allows to load a scaled down version of the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
//          load the bitmap from its path
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            assert scaledBitmap != null;
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileOutputStream out = null;
            //String filename = getFilename();
            try {
                out = new FileOutputStream(filePath);

//          write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            // This is not an image file.
        }


        return filePath;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}
