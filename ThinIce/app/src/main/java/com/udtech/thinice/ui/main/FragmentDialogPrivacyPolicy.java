package com.udtech.thinice.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.udtech.thinice.R;

/**
 * Created by theak on 27.10.2017.
 */

public class FragmentDialogPrivacyPolicy extends DialogFragment {
    private static final String SUPPORT_MAIL = "support@thiniceweightloss.com";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(getContext(), R.layout.fragment_dialog_terms, null);
        dialog.setContentView(view);
        view.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        SpannableString text = new SpannableString(Html.fromHtml( getContext().getResources().getString(R.string.privacy_policy)));
        for(int i = 0; i<text.toString().length();){
            int firstIndex = text.toString().indexOf(SUPPORT_MAIL,i);
            if(firstIndex==-1)
                break;
            ClickableSpan mailClick = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{SUPPORT_MAIL});
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            text.setSpan(mailClick,firstIndex, firstIndex+SUPPORT_MAIL.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            i = firstIndex+SUPPORT_MAIL.length();
        }

        ((TextView) view.findViewById(R.id.text)).setLinksClickable(true);
        ((TextView) view.findViewById(R.id.text)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) view.findViewById(R.id.text)).setText(text, TextView.BufferType.SPANNABLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}