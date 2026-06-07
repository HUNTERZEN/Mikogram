package org.telegram.ui;

import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.MikugramConfig;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MikugramAppearanceActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;
    private int rowCount;

    private int headerRow;
    private int transparentStatusBarRow;
    private int tabletModeRow;
    private int disableAppBarShadowRow;
    private int mediaPreviewRow;
    private int formatTimeWithSecondsRow;
    private int infoRow;

    @Override
    public boolean onFragmentCreate() {
        rowCount = 0;
        headerRow = rowCount++;
        transparentStatusBarRow = rowCount++;
        tabletModeRow = rowCount++;
        disableAppBarShadowRow = rowCount++;
        mediaPreviewRow = rowCount++;
        formatTimeWithSecondsRow = rowCount++;
        infoRow = rowCount++;
        return super.onFragmentCreate();
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(getString(R.string.MikugramAppearance));
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
            if (position == transparentStatusBarRow) {
                MikugramConfig.toggleTransparentStatusBar();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isTransparentStatusBar());
            } else if (position == tabletModeRow) {
                showTabletModeDialog();
            } else if (position == disableAppBarShadowRow) {
                MikugramConfig.toggleDisableAppBarShadow();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isDisableAppBarShadow());
            } else if (position == mediaPreviewRow) {
                MikugramConfig.toggleMediaPreview();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isMediaPreview());
            } else if (position == formatTimeWithSecondsRow) {
                MikugramConfig.toggleFormatTimeWithSeconds();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isFormatTimeWithSeconds());
            }
        });

        return fragmentView;
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
            if (listAdapter != null) listAdapter.notifyItemChanged(tabletModeRow);
        });
        showDialog(builder.create());
    }

    private String getTabletModeText() {
        switch (MikugramConfig.getTabletMode()) {
            case MikugramConfig.TABLET_ENABLE: return getString(R.string.MikugramTabletEnable);
            case MikugramConfig.TABLET_DISABLE: return getString(R.string.MikugramTabletDisable);
            default: return getString(R.string.MikugramTabletAuto);
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        public ListAdapter(Context context) { mContext = context; }

        @Override public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == 1 || type == 3;
        }
        @Override public int getItemCount() { return rowCount; }

        @NonNull @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0: view = new HeaderCell(mContext); view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite)); break;
                case 1: view = new TextCheckCell(mContext); view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite)); break;
                case 3: view = new TextSettingsCell(mContext); view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite)); break;
                default: view = new TextInfoPrivacyCell(mContext); break;
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    ((HeaderCell) holder.itemView).setText(getString(R.string.MikugramAppearance));
                    break;
                case 1: {
                    TextCheckCell cell = (TextCheckCell) holder.itemView;
                    if (position == transparentStatusBarRow) cell.setTextAndCheck(getString(R.string.MikugramTransparentStatusBar), MikugramConfig.isTransparentStatusBar(), true);
                    else if (position == disableAppBarShadowRow) cell.setTextAndCheck(getString(R.string.MikugramDisableAppBarShadow), MikugramConfig.isDisableAppBarShadow(), true);
                    else if (position == mediaPreviewRow) cell.setTextAndCheck(getString(R.string.MikugramMediaPreview), MikugramConfig.isMediaPreview(), true);
                    else if (position == formatTimeWithSecondsRow) cell.setTextAndCheck(getString(R.string.MikugramFormatTimeWithSeconds), MikugramConfig.isFormatTimeWithSeconds(), false);
                    break;
                }
                case 3: {
                    TextSettingsCell cell = (TextSettingsCell) holder.itemView;
                    if (position == tabletModeRow) cell.setTextAndValue(getString(R.string.MikugramTabletMode), getTabletModeText(), true);
                    break;
                }
                case 2:
                    ((TextInfoPrivacyCell) holder.itemView).setText(getString(R.string.MikugramAppearanceInfo));
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == headerRow) return 0;
            if (position == tabletModeRow) return 3;
            if (position == infoRow) return 2;
            return 1;
        }
    }
}
