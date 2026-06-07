package org.telegram.ui;

import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MikugramConfig;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

import java.util.ArrayList;

public class MikugramPreferencesActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;

    // Row indices
    private int rowCount;

    // ── General ──
    private int generalHeaderRow;
    private int useSystemEmojiRow;
    private int disableNumberRoundingRow;
    private int askBeforeCallRow;
    private int openArchiveOnPullRow;
    private int preferIPv6Row;
    private int generalInfoRow;

    // ── Chats ──
    private int chatsHeaderRow;
    private int ignoreBlockedRow;
    private int hideKeyboardOnScrollRow;
    private int disableGreetingStickerRow;
    private int disableJumpToNextRow;
    private int disableVoiceAutoplayRow;
    private int confirmAVMessageRow;
    private int autoPauseVideoRow;
    private int disableProximityRow;
    private int disableInstantCameraRow;
    private int unmuteWithVolumeRow;
    private int hideTimeOnStickerRow;
    private int doubleTapActionRow;
    private int chatsInfoRow;

    // ── Appearance ──
    private int appearanceHeaderRow;
    private int transparentStatusBarRow;
    private int tabletModeRow;
    private int disableAppBarShadowRow;
    private int mediaPreviewRow;
    private int formatTimeWithSecondsRow;
    private int appearanceInfoRow;

    // ── Network & Experimental ──
    private int experimentalHeaderRow;
    private int showNetworkSpeedRow;
    private int downloadSpeedBoostRow;
    private int showRPCErrorRow;
    private int mapDriftingFixRow;
    private int experimentalInfoRow;

    // ── About ──
    private int aboutHeaderRow;
    private int versionRow;
    private int aboutShadowRow;

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

        // Chats
        chatsHeaderRow = rowCount++;
        ignoreBlockedRow = rowCount++;
        hideKeyboardOnScrollRow = rowCount++;
        disableGreetingStickerRow = rowCount++;
        disableJumpToNextRow = rowCount++;
        disableVoiceAutoplayRow = rowCount++;
        confirmAVMessageRow = rowCount++;
        autoPauseVideoRow = rowCount++;
        disableProximityRow = rowCount++;
        disableInstantCameraRow = rowCount++;
        unmuteWithVolumeRow = rowCount++;
        hideTimeOnStickerRow = rowCount++;
        doubleTapActionRow = rowCount++;
        chatsInfoRow = rowCount++;

        // Appearance
        appearanceHeaderRow = rowCount++;
        transparentStatusBarRow = rowCount++;
        tabletModeRow = rowCount++;
        disableAppBarShadowRow = rowCount++;
        mediaPreviewRow = rowCount++;
        formatTimeWithSecondsRow = rowCount++;
        appearanceInfoRow = rowCount++;

        // Network & Experimental
        experimentalHeaderRow = rowCount++;
        showNetworkSpeedRow = rowCount++;
        downloadSpeedBoostRow = rowCount++;
        showRPCErrorRow = rowCount++;
        mapDriftingFixRow = rowCount++;
        experimentalInfoRow = rowCount++;

        // About
        aboutHeaderRow = rowCount++;
        versionRow = rowCount++;
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
            // ── General toggles ──
            if (position == useSystemEmojiRow) {
                MikugramConfig.toggleUseSystemEmoji();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isUseSystemEmoji());
                }
            } else if (position == disableNumberRoundingRow) {
                MikugramConfig.toggleDisableNumberRounding();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isDisableNumberRounding());
                }
            } else if (position == askBeforeCallRow) {
                MikugramConfig.toggleAskBeforeCall();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isAskBeforeCall());
                }
            } else if (position == openArchiveOnPullRow) {
                MikugramConfig.toggleOpenArchiveOnPull();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isOpenArchiveOnPull());
                }
            } else if (position == preferIPv6Row) {
                MikugramConfig.togglePreferIPv6();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isPreferIPv6());
                }

            // ── Chats toggles ──
            } else if (position == ignoreBlockedRow) {
                MikugramConfig.toggleIgnoreBlocked();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isIgnoreBlocked());
                }
            } else if (position == hideKeyboardOnScrollRow) {
                MikugramConfig.toggleHideKeyboardOnScroll();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isHideKeyboardOnScroll());
                }
            } else if (position == disableGreetingStickerRow) {
                MikugramConfig.toggleDisableGreetingSticker();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isDisableGreetingSticker());
                }
            } else if (position == disableJumpToNextRow) {
                MikugramConfig.toggleDisableJumpToNext();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isDisableJumpToNext());
                }
            } else if (position == disableVoiceAutoplayRow) {
                MikugramConfig.toggleDisableVoiceAutoplay();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isDisableVoiceAutoplay());
                }
            } else if (position == confirmAVMessageRow) {
                MikugramConfig.toggleConfirmAVMessage();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isConfirmAVMessage());
                }
            } else if (position == autoPauseVideoRow) {
                MikugramConfig.toggleAutoPauseVideo();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isAutoPauseVideo());
                }
            } else if (position == disableProximityRow) {
                MikugramConfig.toggleDisableProximity();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isDisableProximity());
                }
            } else if (position == disableInstantCameraRow) {
                MikugramConfig.toggleDisableInstantCamera();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isDisableInstantCamera());
                }
            } else if (position == unmuteWithVolumeRow) {
                MikugramConfig.toggleUnmuteWithVolume();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isUnmuteWithVolume());
                }
            } else if (position == hideTimeOnStickerRow) {
                MikugramConfig.toggleHideTimeOnSticker();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isHideTimeOnSticker());
                }
            } else if (position == doubleTapActionRow) {
                showDoubleTapActionDialog();

            // ── Appearance toggles ──
            } else if (position == transparentStatusBarRow) {
                MikugramConfig.toggleTransparentStatusBar();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isTransparentStatusBar());
                }
            } else if (position == tabletModeRow) {
                showTabletModeDialog();
            } else if (position == disableAppBarShadowRow) {
                MikugramConfig.toggleDisableAppBarShadow();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isDisableAppBarShadow());
                }
            } else if (position == mediaPreviewRow) {
                MikugramConfig.toggleMediaPreview();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isMediaPreview());
                }
            } else if (position == formatTimeWithSecondsRow) {
                MikugramConfig.toggleFormatTimeWithSeconds();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isFormatTimeWithSeconds());
                }

            // ── Network & Experimental toggles ──
            } else if (position == showNetworkSpeedRow) {
                MikugramConfig.toggleShowNetworkSpeed();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isShowNetworkSpeed());
                }
                if (MikugramConfig.isShowNetworkSpeed()) {
                    org.telegram.messenger.NetworkSpeedMonitor.getInstance().start();
                } else {
                    org.telegram.messenger.NetworkSpeedMonitor.getInstance().stop();
                }
            } else if (position == downloadSpeedBoostRow) {
                MikugramConfig.toggleDownloadSpeedBoost();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isDownloadSpeedBoost());
                }
            } else if (position == showRPCErrorRow) {
                MikugramConfig.toggleShowRPCError();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isShowRPCError());
                }
            } else if (position == mapDriftingFixRow) {
                MikugramConfig.toggleMapDriftingFix();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isMapDriftingFix());
                }
            }
        });

        return fragmentView;
    }

    private void showDoubleTapActionDialog() {
        if (getParentActivity() == null) return;

        String[] options = {
            getString(R.string.MikugramDoubleTapNone),
            getString(R.string.MikugramDoubleTapReaction),
            getString(R.string.MikugramDoubleTapReply),
            getString(R.string.MikugramDoubleTapSave),
            getString(R.string.MikugramDoubleTapEdit)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(getString(R.string.MikugramDoubleTapAction));
        int currentAction = MikugramConfig.getDoubleTapAction();
        builder.setItems(options, (dialog, which) -> {
            MikugramConfig.setDoubleTapAction(which);
            if (listAdapter != null) {
                listAdapter.notifyItemChanged(doubleTapActionRow);
            }
        });
        showDialog(builder.create());
    }

    private void showTabletModeDialog() {
        if (getParentActivity() == null) return;

        String[] options = {
            getString(R.string.MikugramTabletAuto),
            getString(R.string.MikugramTabletEnable),
            getString(R.string.MikugramTabletDisable)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(getString(R.string.MikugramTabletMode));
        builder.setItems(options, (dialog, which) -> {
            MikugramConfig.setTabletMode(which);
            if (listAdapter != null) {
                listAdapter.notifyItemChanged(tabletModeRow);
            }
        });
        showDialog(builder.create());
    }

    private String getDoubleTapActionText() {
        switch (MikugramConfig.getDoubleTapAction()) {
            case MikugramConfig.DOUBLE_TAP_REACTION:
                return getString(R.string.MikugramDoubleTapReaction);
            case MikugramConfig.DOUBLE_TAP_REPLY:
                return getString(R.string.MikugramDoubleTapReply);
            case MikugramConfig.DOUBLE_TAP_SAVE:
                return getString(R.string.MikugramDoubleTapSave);
            case MikugramConfig.DOUBLE_TAP_EDIT:
                return getString(R.string.MikugramDoubleTapEdit);
            default:
                return getString(R.string.MikugramDoubleTapNone);
        }
    }

    private String getTabletModeText() {
        switch (MikugramConfig.getTabletMode()) {
            case MikugramConfig.TABLET_ENABLE:
                return getString(R.string.MikugramTabletEnable);
            case MikugramConfig.TABLET_DISABLE:
                return getString(R.string.MikugramTabletDisable);
            default:
                return getString(R.string.MikugramTabletAuto);
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private Context mContext;

        private static final int VIEW_TYPE_HEADER = 0;
        private static final int VIEW_TYPE_CHECK = 1;
        private static final int VIEW_TYPE_INFO = 2;
        private static final int VIEW_TYPE_SHADOW = 3;
        private static final int VIEW_TYPE_SETTINGS = 4;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == VIEW_TYPE_CHECK || type == VIEW_TYPE_SETTINGS;
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
                case VIEW_TYPE_HEADER:
                    view = new HeaderCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case VIEW_TYPE_CHECK:
                    view = new TextCheckCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case VIEW_TYPE_INFO:
                    view = new TextInfoPrivacyCell(mContext);
                    break;
                case VIEW_TYPE_SHADOW:
                    view = new ShadowSectionCell(mContext);
                    break;
                case VIEW_TYPE_SETTINGS:
                default:
                    view = new TextSettingsCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_HEADER: {
                    HeaderCell cell = (HeaderCell) holder.itemView;
                    if (position == generalHeaderRow) {
                        cell.setText(getString(R.string.MikugramGeneral));
                    } else if (position == chatsHeaderRow) {
                        cell.setText(getString(R.string.MikugramChats));
                    } else if (position == appearanceHeaderRow) {
                        cell.setText(getString(R.string.MikugramAppearance));
                    } else if (position == experimentalHeaderRow) {
                        cell.setText(getString(R.string.MikugramExperimental));
                    } else if (position == aboutHeaderRow) {
                        cell.setText(getString(R.string.MikugramAbout));
                    }
                    break;
                }
                case VIEW_TYPE_CHECK: {
                    TextCheckCell cell = (TextCheckCell) holder.itemView;
                    boolean divider = true;

                    // General
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

                    // Chats
                    } else if (position == ignoreBlockedRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramIgnoreBlocked), MikugramConfig.isIgnoreBlocked(), true);
                    } else if (position == hideKeyboardOnScrollRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramHideKeyboardOnScroll), MikugramConfig.isHideKeyboardOnScroll(), true);
                    } else if (position == disableGreetingStickerRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramDisableGreetingSticker), MikugramConfig.isDisableGreetingSticker(), true);
                    } else if (position == disableJumpToNextRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramDisableJumpToNext), MikugramConfig.isDisableJumpToNext(), true);
                    } else if (position == disableVoiceAutoplayRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramDisableVoiceAutoplay), MikugramConfig.isDisableVoiceAutoplay(), true);
                    } else if (position == confirmAVMessageRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramConfirmAVMessage), MikugramConfig.isConfirmAVMessage(), true);
                    } else if (position == autoPauseVideoRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramAutoPauseVideo), MikugramConfig.isAutoPauseVideo(), true);
                    } else if (position == disableProximityRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramDisableProximity), MikugramConfig.isDisableProximity(), true);
                    } else if (position == disableInstantCameraRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramDisableInstantCamera), MikugramConfig.isDisableInstantCamera(), true);
                    } else if (position == unmuteWithVolumeRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramUnmuteWithVolume), MikugramConfig.isUnmuteWithVolume(), true);
                    } else if (position == hideTimeOnStickerRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramHideTimeOnSticker), MikugramConfig.isHideTimeOnSticker(), true);

                    // Appearance
                    } else if (position == transparentStatusBarRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramTransparentStatusBar), MikugramConfig.isTransparentStatusBar(), true);
                    } else if (position == disableAppBarShadowRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramDisableAppBarShadow), MikugramConfig.isDisableAppBarShadow(), true);
                    } else if (position == mediaPreviewRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramMediaPreview), MikugramConfig.isMediaPreview(), true);
                    } else if (position == formatTimeWithSecondsRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramFormatTimeWithSeconds), MikugramConfig.isFormatTimeWithSeconds(), false);

                    // Network & Experimental
                    } else if (position == showNetworkSpeedRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramShowNetworkSpeed), MikugramConfig.isShowNetworkSpeed(), true);
                    } else if (position == downloadSpeedBoostRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramDownloadSpeedBoost), MikugramConfig.isDownloadSpeedBoost(), true);
                    } else if (position == showRPCErrorRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramShowRPCError), MikugramConfig.isShowRPCError(), true);
                    } else if (position == mapDriftingFixRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramMapDriftingFix), MikugramConfig.isMapDriftingFix(), false);
                    }
                    break;
                }
                case VIEW_TYPE_INFO: {
                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == generalInfoRow) {
                        cell.setText(getString(R.string.MikugramPreferIPv6Info));
                    } else if (position == chatsInfoRow) {
                        cell.setText(getString(R.string.MikugramChatsInfo));
                    } else if (position == appearanceInfoRow) {
                        cell.setText(getString(R.string.MikugramAppearanceInfo));
                    } else if (position == experimentalInfoRow) {
                        cell.setText(getString(R.string.MikugramExperimentalInfo));
                    }
                    break;
                }
                case VIEW_TYPE_SETTINGS: {
                    TextSettingsCell cell = (TextSettingsCell) holder.itemView;
                    if (position == doubleTapActionRow) {
                        cell.setTextAndValue(getString(R.string.MikugramDoubleTapAction), getDoubleTapActionText(), true);
                    } else if (position == tabletModeRow) {
                        cell.setTextAndValue(getString(R.string.MikugramTabletMode), getTabletModeText(), true);
                    } else if (position == versionRow) {
                        try {
                            android.content.pm.PackageInfo pInfo = ApplicationLoader.applicationContext.getPackageManager()
                                    .getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                            cell.setTextAndValue(getString(R.string.MikugramVersion), "v" + pInfo.versionName, false);
                        } catch (Exception e) {
                            cell.setTextAndValue(getString(R.string.MikugramVersion), "Unknown", false);
                        }
                    }
                    break;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == generalHeaderRow || position == chatsHeaderRow ||
                position == appearanceHeaderRow || position == experimentalHeaderRow ||
                position == aboutHeaderRow) {
                return VIEW_TYPE_HEADER;
            } else if (position == generalInfoRow || position == chatsInfoRow ||
                       position == appearanceInfoRow || position == experimentalInfoRow) {
                return VIEW_TYPE_INFO;
            } else if (position == aboutShadowRow) {
                return VIEW_TYPE_SHADOW;
            } else if (position == doubleTapActionRow || position == tabletModeRow || position == versionRow) {
                return VIEW_TYPE_SETTINGS;
            } else {
                // All remaining rows are check cells
                return VIEW_TYPE_CHECK;
            }
        }
    }
}
