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

    private int appearanceHeaderRow;
    private int darkAmoledRow;
    private int darkAmoledInfoRow;

    private int networkHeaderRow;
    private int showNetworkSpeedRow;
    private int networkSpeedInfoRow;

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

        appearanceHeaderRow = rowCount++;
        darkAmoledRow = rowCount++;
        darkAmoledInfoRow = rowCount++;

        networkHeaderRow = rowCount++;
        showNetworkSpeedRow = rowCount++;
        networkSpeedInfoRow = rowCount++;

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
            if (position == darkAmoledRow) {
                MikugramConfig.toggleDarkAmoled();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isDarkAmoled());
                }
                // Force theme refresh
                Theme.reloadAllResources(getParentActivity());
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, Theme.getActiveTheme(), false, null, -1);
            } else if (position == showNetworkSpeedRow) {
                MikugramConfig.toggleShowNetworkSpeed();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(MikugramConfig.isShowNetworkSpeed());
                }
                // Start or stop the network monitor
                if (MikugramConfig.isShowNetworkSpeed()) {
                    org.telegram.messenger.NetworkSpeedMonitor.getInstance().start();
                } else {
                    org.telegram.messenger.NetworkSpeedMonitor.getInstance().stop();
                }
            }
        });

        return fragmentView;
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
                    if (position == appearanceHeaderRow) {
                        cell.setText(getString(R.string.MikugramAppearance));
                    } else if (position == networkHeaderRow) {
                        cell.setText(getString(R.string.MikugramNetwork));
                    } else if (position == aboutHeaderRow) {
                        cell.setText(getString(R.string.MikugramAbout));
                    }
                    break;
                }
                case VIEW_TYPE_CHECK: {
                    TextCheckCell cell = (TextCheckCell) holder.itemView;
                    if (position == darkAmoledRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramDarkAmoled), MikugramConfig.isDarkAmoled(), true);
                    } else if (position == showNetworkSpeedRow) {
                        cell.setTextAndCheck(getString(R.string.MikugramShowNetworkSpeed), MikugramConfig.isShowNetworkSpeed(), false);
                    }
                    break;
                }
                case VIEW_TYPE_INFO: {
                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == darkAmoledInfoRow) {
                        cell.setText(getString(R.string.MikugramDarkAmoledInfo));
                    } else if (position == networkSpeedInfoRow) {
                        cell.setText(getString(R.string.MikugramShowNetworkSpeedInfo));
                    }
                    break;
                }
                case VIEW_TYPE_SETTINGS: {
                    TextSettingsCell cell = (TextSettingsCell) holder.itemView;
                    if (position == versionRow) {
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
            if (position == appearanceHeaderRow || position == networkHeaderRow || position == aboutHeaderRow) {
                return VIEW_TYPE_HEADER;
            } else if (position == darkAmoledRow || position == showNetworkSpeedRow) {
                return VIEW_TYPE_CHECK;
            } else if (position == darkAmoledInfoRow || position == networkSpeedInfoRow) {
                return VIEW_TYPE_INFO;
            } else if (position == aboutShadowRow) {
                return VIEW_TYPE_SHADOW;
            } else if (position == versionRow) {
                return VIEW_TYPE_SETTINGS;
            }
            return VIEW_TYPE_SHADOW;
        }
    }
}
