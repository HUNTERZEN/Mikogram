package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;
import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MikugramConfig;
import org.telegram.messenger.MikugramHaptics;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MikugramPreferencesActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;

    private int rowCount;

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
            // Privacy toggles
            if (position == hidePhoneFromBotsRow) {
                MikugramConfig.toggleHidePhoneFromBots();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isHidePhoneFromBots());
                MikugramHaptics.performHapticFeedback(view);
            } else if (position == hideOnlineRow) {
                MikugramConfig.toggleHideOnline();
                boolean isHide = MikugramConfig.isHideOnline();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(isHide);
                MikugramHaptics.performHapticFeedback(view);
                for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
                    if (UserConfig.getInstance(a).isClientActivated()) {
                        MessagesController.getInstance(a).syncHideOnline(isHide);
                    }
                }
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
        private static final int VIEW_TYPE_SECTION_HEADER = 4;

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
                    if (position == hidePhoneFromBotsRow) {
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
                    }
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
            if (position == privacyHeaderRow) {
                return VIEW_TYPE_SECTION_HEADER;
            }
            if (position == hidePhoneFromBotsRow || position == hideOnlineRow ||
                position == hideTypingRow || position == hideSeenRow) {
                return VIEW_TYPE_CHECK;
            }
            if (position == privacyShadowRow ||
                position == categoriesShadowRow || position == aboutShadowRow) {
                return VIEW_TYPE_SHADOW;
            }
            if (position == categoriesHeaderRow || position == aboutHeaderRow) {
                return VIEW_TYPE_HEADER;
            }
            if (position == generalRow || position == appearanceRow ||
                position == chatRow || position == experimentalRow ||
                position == versionRow || position == sourceCodeRow) {
                return VIEW_TYPE_TEXT_CELL;
            }
            return VIEW_TYPE_SHADOW;
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
        if (position == privacyHeaderRow) {
            titleView.setText(getString(R.string.MikugramPrivacy));
            subtitleView.setText(getString(R.string.MikugramPrivacyInfo));
        }
    }
}
