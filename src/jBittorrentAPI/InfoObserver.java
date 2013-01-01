package jBittorrentAPI;

public interface InfoObserver {
   void notify(float done, float downSpeed);
}
