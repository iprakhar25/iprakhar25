#!/usr/bin/env python3
"""
YouTube Music Fetcher Script
Fetches currently playing or last played song from YouTube Music
Requires: ytmusicapi library installed
"""

import json
import sys
import os
from datetime import datetime
from pathlib import Path

def get_youtube_music_song():
    """
    Fetch song from YouTube Music account
    Returns JSON with: songName, artist, albumArt, status, lastUpdated
    """
    try:
        # Import ytmusicapi (will fail gracefully if not installed)
        from ytmusicapi import YTMusic
        
        print("[PYTHON] Initializing YouTube Music API...", file=sys.stderr)
        
        # Initialize with headers (no password needed, uses browser authentication)
        # The headers file should be created manually using ytmusicapi setup
        headers_file = os.path.expanduser('~/.config/ytmusicapi/headers_auth.json')
        
        if not os.path.exists(headers_file):
            raise FileNotFoundError(
                f"YouTube Music auth headers not found at {headers_file}\n"
                "Please run: ytmusicapi oauth to setup authentication"
            )
        
        # Initialize YTMusic with OAuth
        yt = YTMusic(headers_file)
        
        print("[PYTHON] Connected to YouTube Music", file=sys.stderr)
        
        # Try to get currently playing track
        print("[PYTHON] Fetching currently playing track...", file=sys.stderr)
        
        # Get watch history (first item is last played)
        history = yt.get_watch_history()
        
        if history and len(history) > 0:
            track = history[0]
            
            song_name = track.get('title', 'Unknown Song')
            artist = ', '.join([a['name'] for a in track.get('artists', [])]) if track.get('artists') else 'Unknown Artist'
            album_art = track.get('thumbnails', [{}])[-1].get('url', '') if track.get('thumbnails') else ''
            
            # Determine if currently playing or last played
            # For now, we'll assume it's the last played since we don't have real-time data
            status = "LAST_PLAYED"
            
            # Create response
            response = {
                'songName': song_name,
                'artist': artist,
                'albumArt': album_art,
                'status': status,
                'lastUpdated': datetime.now().isoformat()
            }
            
            # Output JSON to stdout (this is what Java will read)
            print(json.dumps(response), end='')
            return 0
            
        else:
            # No history found
            raise Exception("No songs found in YouTube Music history")
            
    except ImportError:
        raise ImportError("ytmusicapi library not installed. Install with: pip install ytmusicapi")
    except FileNotFoundError as e:
        raise Exception(f"YouTube Music authentication not configured: {str(e)}")
    except Exception as e:
        raise Exception(f"Error fetching from YouTube Music: {str(e)}")

def main():
    """Main entry point"""
    try:
        exit_code = get_youtube_music_song()
        sys.exit(exit_code)
    except Exception as e:
        # Print error to stderr (won't interfere with JSON output to stdout)
        error_response = {
            'songName': 'Error',
            'artist': str(e),
            'albumArt': '',
            'status': 'ERROR',
            'lastUpdated': datetime.now().isoformat()
        }
        print(json.dumps(error_response), end='')
        print(f"[PYTHON ERROR] {str(e)}", file=sys.stderr)
        sys.exit(1)

if __name__ == '__main__':
    main()
