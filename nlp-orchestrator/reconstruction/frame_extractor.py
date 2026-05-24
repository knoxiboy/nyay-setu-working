import cv2
import os

video_path = r"input_videos\Sample2.mp4"
output_folder = r"reconstruction\output_frames\cam2"

os.makedirs(output_folder, exist_ok=True)

cap = cv2.VideoCapture(video_path)

if not cap.isOpened():
    print("Error opening video")
    exit()

fps = cap.get(cv2.CAP_PROP_FPS)

frame_number = 0

while True:

    success, frame = cap.read()

    if not success:
        break

    timestamp = frame_number / fps

    frame_filename = os.path.join(
        output_folder,
        f"frame_{frame_number:04d}.jpg"
    )

    cv2.imwrite(frame_filename, frame)

    print(f"Saved frame at {timestamp:.2f} sec")

    frame_number += 1

cap.release()

print("Frame extraction completed")