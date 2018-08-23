package com.mothership.servicedecoratorsample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import co.poynt.os.Constants;
import co.poynt.os.model.PrintedReceipt;
import co.poynt.os.model.PrintedReceiptLine;
import co.poynt.os.printing.ReceiptPrintingPref;
import co.poynt.os.model.PrintedReceiptSection;
import co.poynt.os.services.v1.IPoyntReceiptDecoratorService;
import co.poynt.os.services.v1.IPoyntReceiptDecoratorServiceListener;

/**
 * Created by dennis on 8/27/17.
 */

public class ReceiptCustomizationService extends Service {

    private static final String TAG = ReceiptCustomizationService.class.getSimpleName();

    private IPoyntReceiptDecoratorService.Stub mBinder = new IPoyntReceiptDecoratorService.Stub() {
        @Override
        public void decorate(PrintedReceipt printedReceipt, String requestId, IPoyntReceiptDecoratorServiceListener
                listener) throws RemoteException {
            List<PrintedReceiptLine> headerLines = printedReceipt.getHeader();

            if (headerLines == null) {
                headerLines = new ArrayList<>();
            }

            for (PrintedReceiptLine line : headerLines) {
                Log.d(TAG, "decorate: " + line.getText());
            }
            PrintedReceiptLine EMPTY_LINE = new PrintedReceiptLine();
            EMPTY_LINE.setText("\n");

            PrintedReceiptLine twitterInfo = new PrintedReceiptLine();
            twitterInfo.setText("We are on twitter @poynt");

            headerLines.add(EMPTY_LINE);
            headerLines.add(twitterInfo);
            headerLines.add(EMPTY_LINE);
            headerLines.add(EMPTY_LINE);
            headerLines.add(EMPTY_LINE);

            printedReceipt.setHeader(headerLines);
            listener.onReceiptDecorated(requestId, printedReceipt);
        }

        @Override
        public void cancel(String requestId) throws RemoteException {

        }

        @Override
        public void decorateV2(co.poynt.os.model.PrintedReceiptV2 printedReceiptV2,
                               String requestId, co.poynt.os.services.v1.IPoyntReceiptDecoratorListener listener) throws RemoteException {
            PrintedReceiptSection footerSection = printedReceiptV2.getFooter();
            List<PrintedReceiptLine> headerLines = footerSection.getLines();

            if (headerLines == null) {
                headerLines = new ArrayList<>();
            }

            for (PrintedReceiptLine line : headerLines) {
                Log.d(TAG, "decorate: " + line.getText());
            }
            PrintedReceiptLine EMPTY_LINE = new PrintedReceiptLine();
            EMPTY_LINE.setText("\n");

            PrintedReceiptLine twitterInfo = new PrintedReceiptLine();
            twitterInfo.setText("We are on twitter @poynt");

            headerLines.add(EMPTY_LINE);
            headerLines.add(twitterInfo);
            headerLines.add(EMPTY_LINE);
            headerLines.add(EMPTY_LINE);
            headerLines.add(EMPTY_LINE);

            footerSection.setLines(headerLines);
            printedReceiptV2.setFooter(footerSection);
            // If more than 5 seconds elapsed, connect to IPoyntReceiptPrintingService instead and send control number as a separate print job
            listener.onReceiptDecorated(requestId, printedReceiptV2);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}