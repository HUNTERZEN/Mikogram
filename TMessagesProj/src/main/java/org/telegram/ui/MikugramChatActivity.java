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

public class MikugramChatActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;
    private int rowCount;

    private int headerRow;
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
    private int infoRow;

    @Override
    public boolean onFragmentCreate() {
        rowCount = 0;
        headerRow = rowCount++;
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
        infoRow = rowCount++;
        return super.onFragmentCreate();
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(getString(R.string.MikugramChats));
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
            if (position == ignoreBlockedRow) {
                MikugramConfig.toggleIgnoreBlocked();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isIgnoreBlocked());
            } else if (position == hideKeyboardOnScrollRow) {
                MikugramConfig.toggleHideKeyboardOnScroll();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isHideKeyboardOnScroll());
            } else if (position == disableGreetingStickerRow) {
                MikugramConfig.toggleDisableGreetingSticker();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isDisableGreetingSticker());
            } else if (position == disableJumpToNextRow) {
                MikugramConfig.toggleDisableJumpToNext();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isDisableJumpToNext());
            } else if (position == disableVoiceAutoplayRow) {
                MikugramConfig.toggleDisableVoiceAutoplay();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isDisableVoiceAutoplay());
            } else if (position == confirmAVMessageRow) {
                MikugramConfig.toggleConfirmAVMessage();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isConfirmAVMessage());
            } else if (position == autoPauseVideoRow) {
                MikugramConfig.toggleAutoPauseVideo();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isAutoPauseVideo());
            } else if (position == disableProximityRow) {
                MikugramConfig.toggleDisableProximity();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isDisableProximity());
            } else if (position == disableInstantCameraRow) {
                MikugramConfig.toggleDisableInstantCamera();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isDisableInstantCamera());
            } else if (position == unmuteWithVolumeRow) {
                MikugramConfig.toggleUnmuteWithVolume();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isUnmuteWithVolume());
            } else if (position == hideTimeOnStickerRow) {
                MikugramConfig.toggleHideTimeOnSticker();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isHideTimeOnSticker());
            } else if (position == doubleTapActionRow) {
                showDoubleTapActionDialog();
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
        builder.setItems(options, (dialog, which) -> {
            MikugramConfig.setDoubleTapAction(which);
            if (listAdapter != null) listAdapter.notifyItemChanged(doubleTapActionRow);
        });
        showDialog(builder.create());
    }

    private String getDoubleTapActionText() {
        switch (MikugramConfig.getDoubleTapAction()) {
            case MikugramConfig.DOUBLE_TAP_REACTION: return getString(R.string.MikugramDoubleTapReaction);
            case MikugramConfig.DOUBLE_TAP_REPLY: return getString(R.string.MikugramDoubleTapReply);
            case MikugramConfig.DOUBLE_TAP_SAVE: return getString(R.string.MikugramDoubleTapSave);
            case MikugramConfig.DOUBLE_TAP_EDIT: return getString(R.string.MikugramDoubleTapEdit);
            default: return getString(R.string.MikugramDoubleTapNone);
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
                    ((HeaderCell) holder.itemView).setText(getString(R.string.MikugramChats));
                    break;
                case 1: {
                    TextCheckCell cell = (TextCheckCell) holder.itemView;
                    if (position == ignoreBlockedRow) cell.setTextAndCheck(getString(R.string.MikugramIgnoreBlocked), MikugramConfig.isIgnoreBlocked(), true);
                    else if (position == hideKeyboardOnScrollRow) cell.setTextAndCheck(getString(R.string.MikugramHideKeyboardOnScroll), MikugramConfig.isHideKeyboardOnScroll(), true);
                    else if (position == disableGreetingStickerRow) cell.setTextAndCheck(getString(R.string.MikugramDisableGreetingSticker), MikugramConfig.isDisableGreetingSticker(), true);
                    else if (position == disableJumpToNextRow) cell.setTextAndCheck(getString(R.string.MikugramDisableJumpToNext), MikugramConfig.isDisableJumpToNext(), true);
                    else if (position == disableVoiceAutoplayRow) cell.setTextAndCheck(getString(R.string.MikugramDisableVoiceAutoplay), MikugramConfig.isDisableVoiceAutoplay(), true);
                    else if (position == confirmAVMessageRow) cell.setTextAndCheck(getString(R.string.MikugramConfirmAVMessage), MikugramConfig.isConfirmAVMessage(), true);
                    else if (position == autoPauseVideoRow) cell.setTextAndCheck(getString(R.string.MikugramAutoPauseVideo), MikugramConfig.isAutoPauseVideo(), true);
                    else if (position == disableProximityRow) cell.setTextAndCheck(getString(R.string.MikugramDisableProximity), MikugramConfig.isDisableProximity(), true);
                    else if (position == disableInstantCameraRow) cell.setTextAndCheck(getString(R.string.MikugramDisableInstantCamera), MikugramConfig.isDisableInstantCamera(), true);
                    else if (position == unmuteWithVolumeRow) cell.setTextAndCheck(getString(R.string.MikugramUnmuteWithVolume), MikugramConfig.isUnmuteWithVolume(), true);
                    else if (position == hideTimeOnStickerRow) cell.setTextAndCheck(getString(R.string.MikugramHideTimeOnSticker), MikugramConfig.isHideTimeOnSticker(), true);
                    break;
                }
                case 3: {
                    TextSettingsCell cell = (TextSettingsCell) holder.itemView;
                    if (position == doubleTapActionRow) cell.setTextAndValue(getString(R.string.MikugramDoubleTapAction), getDoubleTapActionText(), false);
                    break;
                }
                case 2:
                    ((TextInfoPrivacyCell) holder.itemView).setText(getString(R.string.MikugramChatsInfo));
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == headerRow) return 0;
            if (position == doubleTapActionRow) return 3;
            if (position == infoRow) return 2;
            return 1;
        }
    }
}
