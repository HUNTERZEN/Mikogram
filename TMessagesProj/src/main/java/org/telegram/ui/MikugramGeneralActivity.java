package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;
import static org.telegram.messenger.LocaleController.getString;

import android.app.AlertDialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FlagSecureReason;
import org.telegram.messenger.MikugramConfig;
import org.telegram.messenger.MikugramHaptics;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MikugramGeneralActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;
    private int rowCount;

    // General section
    private int generalHeaderRow;
    private int useSystemEmojiRow;
    private int disableNumberRoundingRow;
    private int askBeforeCallRow;
    private int openArchiveOnPullRow;
    private int preferIPv6Row;
    private int generalInfoRow;

    // Mods section
    private int modsHeaderRow;
    private int downloadSpeedBoostRow;
    private int showDeletedMessagesRow;
    private int enableScreenshotRow;
    private int enableForwardRow;
    private int vibrationRow;
    private int vibrationIntensityRow;
    private int recentStickersLimitRow;
    private int modsShadowRow;

    @Override
    public boolean onFragmentCreate() {
        updateRows();
        return super.onFragmentCreate();
    }

    private void updateRows() {
        rowCount = 0;

        // General
        generalHeaderRow = rowCount++;
        useSystemEmojiRow = rowCount++;
        disableNumberRoundingRow = rowCount++;
        askBeforeCallRow = rowCount++;
        openArchiveOnPullRow = rowCount++;
        preferIPv6Row = rowCount++;
        generalInfoRow = rowCount++;

        // Mods
        modsHeaderRow = rowCount++;
        downloadSpeedBoostRow = rowCount++;
        showDeletedMessagesRow = rowCount++;
        enableScreenshotRow = rowCount++;
        enableForwardRow = rowCount++;
        vibrationRow = rowCount++;
        vibrationIntensityRow = rowCount++;
        recentStickersLimitRow = rowCount++;
        modsShadowRow = rowCount++;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(getString(R.string.MikugramGeneral));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) finishFragment();
            }
        });

        listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        fragmentView = frameLayout;

        listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(listAdapter);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        listView.setOnItemClickListener((view, position) -> {
            // General toggles
            if (position == useSystemEmojiRow) {
                MikugramConfig.toggleUseSystemEmoji();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isUseSystemEmoji());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == disableNumberRoundingRow) {
                MikugramConfig.toggleDisableNumberRounding();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isDisableNumberRounding());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == askBeforeCallRow) {
                MikugramConfig.toggleAskBeforeCall();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isAskBeforeCall());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == openArchiveOnPullRow) {
                MikugramConfig.toggleOpenArchiveOnPull();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isOpenArchiveOnPull());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == preferIPv6Row) {
                MikugramConfig.togglePreferIPv6();
                boolean newValue = MikugramConfig.isPreferIPv6();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(newValue);
                MikugramHaptics.performHapticFeedback(view);
                for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
                    ConnectionsManager.getInstance(a).setForceTryIpV6(newValue);
                }
            }
            // Mods toggles
            else if (position == downloadSpeedBoostRow) {
                MikugramConfig.toggleDownloadSpeedBoost();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isDownloadSpeedBoost());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == showDeletedMessagesRow) {
                MikugramConfig.toggleShowDeletedMessages();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isShowDeletedMessages());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == enableScreenshotRow) {
                MikugramConfig.toggleEnableScreenshot();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isEnableScreenshot());
                MikugramHaptics.performHapticFeedback(view);
                FlagSecureReason.updateAll();
            } else if (position == enableForwardRow) {
                MikugramConfig.toggleEnableForward();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isEnableForward());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == vibrationRow) {
                MikugramConfig.toggleVibrationEnabled();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isVibrationEnabled());
                MikugramHaptics.performHapticFeedback(view);
                listAdapter.notifyItemChanged(vibrationIntensityRow);
            } else if (position == recentStickersLimitRow) {
                showRecentStickersLimitDialog(context);
                MikugramHaptics.performHapticFeedback(view);
            }
        });

        return fragmentView;
    }

    private void showRecentStickersLimitDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.MikugramRecentStickersLimit));

        final EditText input = new EditText(context);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(MikugramConfig.getRecentStickersLimit()));
        input.setSelection(input.getText().length());
        input.setPadding(dp(24), dp(12), dp(24), dp(12));
        builder.setView(input);

        builder.setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
            try {
                int value = Integer.parseInt(input.getText().toString().trim());
                if (value < 1) value = 1;
                if (value > 500) value = 500;
                MikugramConfig.setRecentStickersLimit(value);
                listAdapter.notifyItemChanged(recentStickersLimitRow);
            } catch (NumberFormatException ignored) {}
        });
        builder.setNegativeButton(getString(android.R.string.cancel), null);
        builder.show();
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private final Context mContext;

        private static final int VIEW_TYPE_HEADER = 0;
        private static final int VIEW_TYPE_CHECK = 1;
        private static final int VIEW_TYPE_INFO = 2;
        private static final int VIEW_TYPE_SECTION_HEADER = 3;
        private static final int VIEW_TYPE_SLIDER = 4;
        private static final int VIEW_TYPE_TEXT_CELL = 5;
        private static final int VIEW_TYPE_SHADOW = 6;

        public ListAdapter(Context context) { mContext = context; }

        @Override public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == VIEW_TYPE_CHECK || type == VIEW_TYPE_TEXT_CELL;
        }

        @Override public int getItemCount() { return rowCount; }

        @NonNull @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case VIEW_TYPE_HEADER: {
                    view = new HeaderCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                }
                case VIEW_TYPE_CHECK: {
                    view = new TextCheckCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                }
                case VIEW_TYPE_INFO: {
                    view = new TextInfoPrivacyCell(mContext);
                    break;
                }
                case VIEW_TYPE_SECTION_HEADER: {
                    view = createSectionHeaderView(mContext);
                    break;
                }
                case VIEW_TYPE_SLIDER: {
                    view = createSliderView(mContext);
                    break;
                }
                case VIEW_TYPE_TEXT_CELL: {
                    view = new TextCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                }
                default: {
                    view = new ShadowSectionCell(mContext);
                    break;
                }
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_HEADER:
                    if (position == generalHeaderRow) {
                        ((HeaderCell) holder.itemView).setText(getString(R.string.MikugramGeneral));
                    }
                    break;
                case VIEW_TYPE_CHECK: {
                    TextCheckCell cell = (TextCheckCell) holder.itemView;
                    if (position == useSystemEmojiRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramUseSystemEmoji), MikugramConfig.isUseSystemEmoji(), true);
                    } else if (position == disableNumberRoundingRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramDisableNumberRounding), MikugramConfig.isDisableNumberRounding(), true);
                    } else if (position == askBeforeCallRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramAskBeforeCall), MikugramConfig.isAskBeforeCall(), true);
                    } else if (position == openArchiveOnPullRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramOpenArchiveOnPull), MikugramConfig.isOpenArchiveOnPull(), true);
                    } else if (position == preferIPv6Row) {
                        cell.setTextAndCheck(getString(R.string.MikugramPreferIPv6), MikugramConfig.isPreferIPv6(), false);
                    } else if (position == downloadSpeedBoostRow) {
                        cell.setTextAndValueAndCheck(getString(R.string.MikugramDownloadSpeedBoost),
                                getString(R.string.MikugramDownloadSpeedBoostInfo),
                                MikugramConfig.isDownloadSpeedBoost(), true, true);
                    } else if (position == showDeletedMessagesRow) {
                        cell.setTextAndValueAndCheck(getString(R.string.MikugramShowDeletedMessages),
                                getString(R.string.MikugramShowDeletedMessagesInfo),
                                MikugramConfig.isShowDeletedMessages(), true, true);
                    } else if (position == enableScreenshotRow) {
                        cell.setTextAndValueAndCheck(getString(R.string.MikugramEnableScreenshot),
                                getString(R.string.MikugramEnableScreenshotInfo),
                                MikugramConfig.isEnableScreenshot(), true, true);
                    } else if (position == enableForwardRow) {
                        cell.setTextAndValueAndCheck(getString(R.string.MikugramEnableForward),
                                getString(R.string.MikugramEnableForwardInfo),
                                MikugramConfig.isEnableForward(), true, true);
                    } else if (position == vibrationRow) {
                        cell.setTextAndValueAndCheck(getString(R.string.MikugramVibration),
                                getString(R.string.MikugramVibrationInfo),
                                MikugramConfig.isVibrationEnabled(), true, true);
                    }
                    break;
                }
                case VIEW_TYPE_INFO:
                    if (position == generalInfoRow) {
                        ((TextInfoPrivacyCell) holder.itemView).setText(getString(R.string.MikugramPreferIPv6Info));
                    }
                    break;
                case VIEW_TYPE_SECTION_HEADER:
                    bindSectionHeaderView(holder.itemView, position);
                    break;
                case VIEW_TYPE_SLIDER:
                    bindSliderView(holder.itemView, position);
                    break;
                case VIEW_TYPE_TEXT_CELL:
                    if (position == recentStickersLimitRow) {
                        ((TextCell) holder.itemView).setTextAndValue(getString(R.string.MikugramRecentStickersLimit),
                                String.valueOf(MikugramConfig.getRecentStickersLimit()), false);
                    }
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == generalHeaderRow) return VIEW_TYPE_HEADER;
            if (position == generalInfoRow) return VIEW_TYPE_INFO;
            if (position == modsHeaderRow) return VIEW_TYPE_SECTION_HEADER;
            if (position == vibrationIntensityRow) return VIEW_TYPE_SLIDER;
            if (position == recentStickersLimitRow) return VIEW_TYPE_TEXT_CELL;
            if (position == modsShadowRow) return VIEW_TYPE_SHADOW;
            return VIEW_TYPE_CHECK;
        }
    }

    private View createSectionHeaderView(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        layout.setPadding(dp(21), dp(15), dp(21), dp(15));

        TextView titleView = new TextView(context);
        titleView.setTag("title");
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        titleView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader));
        titleView.setTypeface(AndroidUtilities.bold());
        layout.addView(titleView, LayoutHelper.createLinear(
                LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 4));

        TextView subtitleView = new TextView(context);
        subtitleView.setTag("subtitle");
        subtitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        subtitleView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));
        layout.addView(subtitleView, LayoutHelper.createLinear(
                LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        return layout;
    }

    private void bindSectionHeaderView(View view, int position) {
        TextView titleView = view.findViewWithTag("title");
        TextView subtitleView = view.findViewWithTag("subtitle");
        if (position == modsHeaderRow) {
            if (titleView != null) titleView.setText(getString(R.string.MikugramMods));
            if (subtitleView != null) subtitleView.setText(getString(R.string.MikugramModsInfo));
        }
    }

    private View createSliderView(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        layout.setPadding(dp(21), dp(8), dp(21), dp(12));

        LinearLayout labelRow = new LinearLayout(context);
        labelRow.setOrientation(LinearLayout.HORIZONTAL);
        labelRow.setGravity(Gravity.CENTER_VERTICAL);
        layout.addView(labelRow, LayoutHelper.createLinear(
                LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 8));

        TextView labelView = new TextView(context);
        labelView.setTag("slider_label");
        labelView.setText(getString(R.string.MikugramVibrationIntensity));
        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        labelView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        labelRow.addView(labelView, new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView valueView = new TextView(context);
        valueView.setTag("slider_value");
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        valueView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
        labelRow.addView(valueView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        SeekBar seekBar = new SeekBar(context);
        seekBar.setTag("slider_seekbar");
        seekBar.setMax(100);
        seekBar.setProgress(MikugramConfig.getVibrationIntensity());
        layout.addView(seekBar, LayoutHelper.createLinear(
                LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                if (fromUser) {
                    valueView.setText(progress + "%");
                    MikugramConfig.setVibrationIntensity(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb) {}

            @Override
            public void onStopTrackingTouch(SeekBar sb) {
                MikugramHaptics.performTickFeedback(sb);
            }
        });

        return layout;
    }

    private void bindSliderView(View view, int position) {
        if (position == vibrationIntensityRow) {
            int intensity = MikugramConfig.getVibrationIntensity();
            TextView valueView = view.findViewWithTag("slider_value");
            SeekBar seekBar = view.findViewWithTag("slider_seekbar");
            if (valueView != null) valueView.setText(intensity + "%");
            if (seekBar != null) seekBar.setProgress(intensity);

            boolean enabled = MikugramConfig.isVibrationEnabled();
            view.setAlpha(enabled ? 1f : 0.4f);
            if (seekBar != null) seekBar.setEnabled(enabled);
        }
    }
}
