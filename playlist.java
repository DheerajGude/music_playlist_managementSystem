
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // MaxHeap class
    static class MaxHeap {
        private int capacity;
        private List<Song> storage;
        private int size;

        public MaxHeap(int capacity) {
            this.capacity = capacity;
            this.storage = new ArrayList<>(capacity);
            this.size = 0;
        }

        private int getParentIndex(int index) {
            return (index - 1) / 2;
        }

        private int getLeftChildIndex(int index) {
            return 2 * index + 1;
        }

        private int getRightChildIndex(int index) {
            return 2 * index + 2;
        }

        private boolean hasParent(int index) {
            return getParentIndex(index) >= 0;
        }

        private boolean hasLeftChild(int index) {
            return getLeftChildIndex(index) < size;
        }

        private boolean hasRightChild(int index) {
            return getRightChildIndex(index) < size;
        }

        private Song parent(int index) {
            return storage.get(getParentIndex(index));
        }

        private Song leftChild(int index) {
            return storage.get(getLeftChildIndex(index));
        }

        private Song rightChild(int index) {
            return storage.get(getRightChildIndex(index));
        }

        private boolean isFull() {
            return size == capacity;
        }

        private void swap(int index1, int index2) {
            Song temp = storage.get(index1);
            storage.set(index1, storage.get(index2));
            storage.set(index2, temp);
        }

        public void insert(Song song) {
            if (isFull()) {
                System.out.println("The Playlist is full");
                return;
            }

            if (size >= storage.size()) {
                storage.add(song);
            } else {
                storage.set(size, song);
            }
            size++;
            heapifyUp();
        }

        private void heapifyUp() {
            int index = size - 1;

            while (hasParent(index) && parent(index).getLikes() < storage.get(index).getLikes()) {
                swap(getParentIndex(index), index);
                index = getParentIndex(index);
            }
        }

        public void deleteSong() {
            if (size == 0) {
                System.out.println("Playlist is empty");
                return;
            }

            Song song = storage.get(0);
            storage.set(0, storage.get(size - 1));
            storage.set(size - 1, null);
            size--;

            System.out.println("Deleted");
            heapifyDown();
        }

        private void heapifyDown() {
            int index = 0;
            while (hasLeftChild(index)) {
                int largerChildIndex = getLeftChildIndex(index);
                if (hasRightChild(index) && rightChild(index).getLikes() > leftChild(index).getLikes()) {
                    largerChildIndex = getRightChildIndex(index);
                }

                if (storage.get(index).getLikes() > storage.get(largerChildIndex).getLikes()) {
                    break;
                } else {
                    swap(index, largerChildIndex);
                }

                index = largerChildIndex;
            }
        }

        public List<Song> printHeap() {
            return new ArrayList<>(storage.subList(0, size));
        }
    }

    // Song class
    static class Song {
        private String name;
        private int likes;

        public Song(String name, int likes) {
            this.name = name;
            this.likes = likes;
        }

        public String getName() {
            return name;
        }

        public int getLikes() {
            return likes;
        }
    }

    // SingerNode class
    static class SingerNode {
        String singer;
        MaxHeap playlist;
        SingerNode prev;
        SingerNode next;

        public SingerNode(String singer, MaxHeap playlist) {
            this.singer = singer;
            this.playlist = playlist;
            this.prev = null;
            this.next = null;
        }
    }

    // SingerList class
    static class SingerList {
        private SingerNode head;
        private SingerNode tail;

        public SingerList() {
            this.head = null;
            this.tail = null;
            addPredefinedSingers();
        }

        private void addPredefinedSingers() {
            String s1 = "Taylor Swift";
            MaxHeap songs1 = new MaxHeap(10);
            songs1.insert(new Song("Bad Blood", 1000));
            songs1.insert(new Song("Fireworks", 20000));
            songs1.insert(new Song("Blank Space", 232653));
            insert(s1, songs1);

            String s2 = "Shreya Ghoshal";
            MaxHeap songs2 = new MaxHeap(10);
            songs2.insert(new Song("Teri Meri", 50045));
            songs2.insert(new Song("Pal", 12465));
            insert(s2, songs2);

            String s3 = "Gautham Karthik";
            MaxHeap songs3 = new MaxHeap(10);
            songs3.insert(new Song("Hoyna", 83898));
            songs3.insert(new Song("Yenno Yenno", 18802));
            songs3.insert(new Song("Hawa Hawa", 234575));
            insert(s3, songs3);
        }

        public void insert(String singer, MaxHeap playlist) {
            SingerNode singerNode = new SingerNode(singer, playlist);

            if (head == null) {
                head = singerNode;
                tail = singerNode;
            } else {
                tail.next = singerNode;
                singerNode.prev = tail;
                tail = singerNode;
            }
        }

        public void addSinger() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter singer name: ");
            String singer = scanner.nextLine();
            MaxHeap playlist = new MaxHeap(10);
            System.out.print("Enter the number of songs for this singer: ");
            int num = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            for (int i = 0; i < num; i++) {
                System.out.print("Enter song name: ");
                String songName = scanner.nextLine();
                System.out.print("Enter song likes: ");
                int songLikes = scanner.nextInt();
                scanner.nextLine();  // Consume newline
                playlist.insert(new Song(songName, songLikes));
            }
            insert(singer, playlist);
            System.out.println(singer + " added successfully!");
        }

        public void deleteSong(String singerName) {
            SingerNode current = head;
            while (current != null) {
                if (current.singer.equals(singerName)) {
                    current.playlist.deleteSong();
                    return;
                }
                current = current.next;
            }
        }

        public void mostPlayedSong() {
            if (head == null) {
                System.out.println("No singers or songs available.");
                return;
            }

            SingerNode current = head;
            while (current != null) {
                if (!current.playlist.printHeap().isEmpty()) {
                    System.out.println("Most played song for " + current.singer + ": " + current.playlist.printHeap().get(0).getName());
                } else {
                    System.out.println("No songs available for " + current.singer);
                }
                current = current.next;
            }
        }

        public void mostLikedSong() {
            if (head == null) {
                System.out.println("No singers or songs available.");
                return;
            }

            Song mostLikedSong = null;
            String mostLikedSinger = null;

            SingerNode current = head;
            while (current != null) {
                if (!current.playlist.printHeap().isEmpty()) {
                    Song currentSong = current.playlist.printHeap().get(0);
                    if (mostLikedSong == null || currentSong.getLikes() > mostLikedSong.getLikes()) {
                        mostLikedSong = currentSong;
                        mostLikedSinger = current.singer;
                    }
                }
                current = current.next;
            }

            if (mostLikedSong != null) {
                System.out.println("Most liked song among all singers: " + mostLikedSong.getName() + " by " + mostLikedSinger);
            } else {
                System.out.println("No songs available.");
            }
        }

        public void deleteSongMenu() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter singer name: ");
            String singer = scanner.nextLine();
            deleteSong(singer);
            System.out.println("Songs deleted from " + singer + "'s playlist.");
        }

        public void searchSong(String songName) {
            SingerNode current = head;
            while (current != null) {
                for (Song song : current.playlist.printHeap()) {
                    if (song.getName().equals(songName)) {
                        System.out.println("Song found: " + song.getName() + " by " + current.singer + ". Likes: " + song.getLikes());
                        return;
                    }
                }
                current = current.next;
            }
            System.out.println("Song not found.");
        }

        public void displayAllSingers() {
            SingerNode current = head;
            if (current == null) {
                System.out.println("No singers or songs available.");
                return;
            }

           
