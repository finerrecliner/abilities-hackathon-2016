IFS=', ' read -r -a words <<< "$1"
for word in "${words[@]}"
do
   first_letter="$(echo $word | head -c 1)"
   curl -O http://www.handspeak.com/word/$first_letter/$word.mp4
done

i=0
for orig_mp4 in *.mp4; do
   ffmpeg -i $orig_mp4 -r 20 $i.mpg
   rm $orig_mp4
   i=$((i+1))
done

cat *.mpg > final.mpg
ffmpeg -i final.mpg -r 20 final.mp4

rm *.mpg   
