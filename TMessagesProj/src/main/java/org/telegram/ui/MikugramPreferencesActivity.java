package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;
import static org.telegram.messenger.LocaleController.getString;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MikugramConfig;
import org.telegram.messenger.MikugramHaptics;
import org.telegram.messenger.R;
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

public class MikugramPreferencesActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;

    private int rowCount;

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

    // Privacy section
    private int privacyHeaderRow;
    private int hidePhoneFromBotsRow;
    private int hideOnlineRow;
    private int hideTypingRow;
    private int hideSeenRow;
    private int privacyShadowRow;

    // Categories (existing sub-activities)
    private int categoriesHeaderRow;
    private int generalRow;
    private int appearanceRow;
    private int chatRow;
    private int experimentalRow;
    private int categoriesShadowRow;

    // About
    private int aboutHeaderRow;
    private int versionRow;
    private int sourceCodeRow;
    private int aboutShadowRow;

    @Override
    public boolean onFragmentCreate() {
        updateRows();
        return super.onFragmentCreate();
    }

    private void updateRows() {
        rowCount = 0;

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

        // Privacy
        privacyHeaderRow = rowCount++;
        hidePhoneFromBotsRow = rowCount++;
        hideOnlineRow = rowCount++;
        hideTypingRow = rowCount++;
        hideSeenRow = rowCount++;
        privacyShadowRow = rowCount++;

        // Categories
        categoriesHeaderRow = rowCount++;
        generalRow = rowCount++;
        appearanceRow = rowCount++;
        chatRow = rowCount++;
        experimentalRow = rowCount++;
        categoriesShadowRow = rowCount++;

        // About
        aboutHeaderRow = rowCount++;
        versionRow = rowCount++;
        sourceCodeRow = rowCount++;
        aboutShadowRow = rowCount++;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(getString(R.string.MikugramPreferences));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
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
            // Mods toggles
            if (position == downloadSpeedBoostRow) {
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
            } else if (position == enableForwardRow) {
                MikugramConfig.toggleEnableForward();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isEnableForward());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == vibrationRow) {
                MikugramConfig.toggleVibrationEnabled();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isVibrationEnabled());
                MikugramHaptics.performHapticFeedback(view);
                // Show/hide intensity row
                listAdapter.notifyItemChanged(vibrationIntensityRow);
            } else if (position == recentStickersLimitRow) {
                showRecentStickersLimitDialog(context);
                MikugramHaptics.performHapticFeedback(view);
            }
            // Privacy toggles
            else if (position == hidePhoneFromBotsRow) {
                MikugramConfig.toggleHidePhoneFromBots();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isHidePhoneFromBots());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == hideOnlineRow) {
                MikugramConfig.toggleHideOnline();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isHideOnline());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == hideTypingRow) {
                MikugramConfig.toggleHideTyping();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isHideTyping());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == hideSeenRow) {
                MikugramConfig.toggleHideSeen();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isHideSeen());
                MikugramHaptics.performHapticFeedback(view);
            }
            // Categories
            else if (position == generalRow) {
                MikugramHaptics.performHapticFeedback(view);
                presentFragment(new MikugramGeneralActivity());
            } else if (position == appearanceRow) {
                MikugramHaptics.performHapticFeedback(view);
                presentFragment(new MikugramAppearanceActivity());
            } else if (position == chatRow) {
                MikugramHaptics.performHapticFeedback(view);
                presentFragment(new MikugramChatActivity());
            } else if (position == experimentalRow) {
                MikugramHaptics.performHapticFeedback(view);
                presentFragment(new MikugramExperimentalActivity());
            } else if (position == sourceCodeRow) {
                MikugramHaptics.performHapticFeedback(view);
                org.telegram.messenger.browser.Browser.openUrl(getParentActivity(), "https://github.com/HUNTERZEN/Mikogram.git");
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

    private String getVersionString() {
        try {
            PackageInfo pInfo = ApplicationLoader.applicationContext.getPackageManager()
                    .getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            return pInfo.versionName + " (" + pInfo.versionCode + ")";
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private Context mContext;

        private static final int VIEW_TYPE_CHECK = 0;
        private static final int VIEW_TYPE_SHADOW = 1;
        private static final int VIEW_TYPE_HEADER = 2;
        private static final int VIEW_TYPE_TEXT_CELL = 3;
        private static final int VIEW_TYPE_SLIDER = 4;
        private static final int VIEW_TYPE_SECTION_HEADER = 5;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == VIEW_TYPE_CHECK || type == VIEW_TYPE_TEXT_CELL;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case VIEW_TYPE_CHECK: {
                    view = new TextCheckCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                }
                case VIEW_TYPE_SHADOW: {
                    view = new ShadowSectionCell(mContext);
                    break;
                }
                case VIEW_TYPE_HEADER: {
                    view = new HeaderCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                }
                case VIEW_TYPE_TEXT_CELL: {
                    view = new TextCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                }
                case VIEW_TYPE_SLIDER: {
                    view = createSliderView(mContext);
                    break;
                }
                case VIEW_TYPE_SECTION_HEADER: {
                    view = createSectionHeaderView(mContext);
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
                case VIEW_TYPE_CHECK: {
                    TextCheckCell cell = (TextCheckCell) holder.itemView;
                    if (position == downloadSpeedBoostRow) {
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
                    } else if (position == hidePhoneFromBotsRow) {
                        cell.setTextAndValueAndCheck(getString(R.string.MikugramHidePhoneFromBots),
                                getString(R.string.MikugramHidePhoneFromBotsInfo),
                                MikugramConfig.isHidePhoneFromBots(), true, true);
                    } else if (position == hideOnlineRow) {
                        cell.setTextAndValueAndCheck(getString(R.string.MikugramHideOnline),
                                getString(R.string.MikugramHideOnlineInfo),
                                MikugramConfig.isHideOnline(), true, true);
                    } else if (position == hideTypingRow) {
                        cell.setTextAndValueAndCheck(getString(R.string.MikugramHideTyping),
                                getString(R.string.MikugramHideTypingInfo),
                                MikugramConfig.isHideTyping(), true, true);
                    } else if (position == hideSeenRow) {
                        cell.setTextAndValueAndCheck(getString(R.string.MikugramHideSeen),
                                getString(R.string.MikugramHideSeenInfo),
                                MikugramConfig.isHideSeen(), true, false);
                    }
                    break;
                }
                case VIEW_TYPE_HEADER: {
                    HeaderCell cell = (HeaderCell) holder.itemView;
                    if (position == categoriesHeaderRow) {
                        cell.setText(getString(R.string.MikugramPreferences));
                    } else if (position == aboutHeaderRow) {
                        cell.setText(getString(R.string.MikugramAbout));
                    }
                    break;
                }
                case VIEW_TYPE_TEXT_CELL: {
                    TextCell cell = (TextCell) holder.itemView;
                    if (position == generalRow) {
                        cell.setTextAndIcon(getString(R.string.MikugramGeneral), R.drawable.msg_media, true);
                    } else if (position == appearanceRow) {
                        cell.setTextAndIcon(getString(R.string.MikugramAppearance), R.drawable.msg_palette, true);
                    } else if (position == chatRow) {
                        cell.setTextAndIcon(getString(R.string.MikugramChats), R.drawable.msg_discussion, true);
                    } else if (position == experimentalRow) {
                        cell.setTextAndIcon(getString(R.string.MikugramExperimental), R.drawable.msg_fave, false);
                    } else if (position == versionRow) {
                        cell.setTextAndValueAndIcon(getString(R.string.MikugramVersion), getVersionString(), R.drawable.msg_info, true);
                    } else if (position == sourceCodeRow) {
                        cell.setTextAndValueAndIcon(getString(R.string.MikugramSourceCode), getString(R.string.MikugramSourceCodeValue), R.drawable.msg_link, false);
                    } else if (position == recentStickersLimitRow) {
                        cell.setTextAndValue(getString(R.string.MikugramRecentStickersLimit),
                                String.valueOf(MikugramConfig.getRecentStickersLimit()), false);
                    }
                    break;
                }
                case VIEW_TYPE_SLIDER: {
                    // Slider view — bind intensity value
                    bindSliderView(holder.itemView, position);
                    break;
                }
                case VIEW_TYPE_SECTION_HEADER: {
                    bindSectionHeaderView(holder.itemView, position);
                    break;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == modsHeaderRow || position == privacyHeaderRow) {
                return VIEW_TYPE_SECTION_HEADER;
            }
            if (position == downloadSpeedBoostRow || position == showDeletedMessagesRow ||
                position == enableScreenshotRow || position == enableForwardRow ||
                position == vibrationRow ||
                position == hidePhoneFromBotsRow || position == hideOnlineRow ||
                position == hideTypingRow || position == hideSeenRow) {
                return VIEW_TYPE_CHECK;
            }
            if (position == modsShadowRow || position == privacyShadowRow ||
                position == categoriesShadowRow || position == aboutShadowRow) {
                return VIEW_TYPE_SHADOW;
            }
            if (position == categoriesHeaderRow || position == aboutHeaderRow) {
                return VIEW_TYPE_HEADER;
            }
            if (position == vibrationIntensityRow) {
                return VIEW_TYPE_SLIDER;
            }
            if (position == generalRow || position == appearanceRow ||
                position == chatRow || position == experimentalRow ||
                position == versionRow || position == sourceCodeRow ||
                position == recentStickersLimitRow) {
                return VIEW_TYPE_TEXT_CELL;
            }
            return VIEW_TYPE_SHADOW;
        }
    }

    /**
     * Creates a section header view with title and subtitle, styled with a light background accent.
     */
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
            titleView.setText(getString(R.string.MikugramMods));
            subtitleView.setText(getString(R.string.MikugramModsInfo));
        } else if (position == privacyHeaderRow) {
            titleView.setText(getString(R.string.MikugramPrivacy));
            subtitleView.setText(getString(R.string.MikugramPrivacyInfo));
        }
    }

    /**
     * Creates the vibration intensity slider view.
     */
    private View createSliderView(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        layout.setPadding(dp(21), dp(8), dp(21), dp(12));

        // Label + percentage row
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

        // SeekBar
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

            // Hide if vibration is disabled
            boolean enabled = MikugramConfig.isVibrationEnabled();
            view.setAlpha(enabled ? 1f : 0.4f);
            if (seekBar != null) seekBar.setEnabled(enabled);
        }
    }
}
