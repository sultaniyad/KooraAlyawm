package com.iyad.sultan.kooraalyawm.Utilities;

public interface PlayerCallBack {

    interface MyCallback{

        void promotePlayerAsCaptainCallBack(Boolean isCaptain,int position);
        void demotePlayerAsCaptainCallBack(Boolean isCaptain,int position);

        void kikPlayerCallBack(Boolean isCaptain, int position);

    }
}
