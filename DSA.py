class MaxHeap:
    def __init__(self, capacity):
        self.capacity = capacity
        self.storage = [0] * capacity
        self.size = 0

    # Helper Methods

    def getParentIndex(self, index):
        return (index - 1) // 2

    def getLeftChildIndex(self, index):
        return 2 * index + 1

    def getRightChildIndex(self, index):
        return 2 * index + 2

    def hasParent(self, index):
        return self.getParentIndex(index) >= 0

    def hasLeftChild(self, index):
        return self.getLeftChildIndex(index) < self.size

    def hasRightChild(self, index):
        return self.getRightChildIndex(index) < self.size

    def parent(self, index):
        return self.storage[self.getParentIndex(index)]

    def leftChild(self, index):
        return self.storage[self.getLeftChildIndex(index)]

    def rightChild(self, index):
        return self.storage[self.getRightChildIndex(index)]

    def isFull(self):
        return self.size == self.capacity

    def swap(self, index1, index2):
        self.storage[index1], self.storage[index2] = self.storage[index2], self.storage[index1]

    def insert(self, song):
        if self.isFull():
            print("The Playlist is full")
            return

        self.storage[self.size] = song
        self.size += 1
        self.heapifyUp()

    def heapifyUp(self):
        index = self.size - 1

        while self.hasParent(index) and self.parent(index).likes < self.storage[index].likes:
            self.swap(self.getParentIndex(index), index)
            index = self.getParentIndex(index)

    def deleteSong(self):
        if self.size == 0:
            print("Playlist is empty")
            return

        song = self.storage[0]
        self.storage[0] = self.storage[self.size - 1]
        self.storage[self.size - 1] = None
        self.size -= 1

        print("Deleted")
        self.heapifyDown()

    def heapifyDown(self):
        index = 0
        while self.hasLeftChild(index):
            largerChildIndex = self.getLeftChildIndex(index)
            if self.hasRightChild(index) and self.rightChild(index).likes > self.leftChild(index).likes:
                largerChildIndex = self.getRightChildIndex(index)

            if self.storage[index].likes > self.storage[largerChildIndex].likes:
                break
            else:
                self.swap(index, largerChildIndex)

            index = largerChildIndex

    def printHeap(self):
        return self.storage[:self.size]


class Song:
    def __init__(self, name, likes):
        self.name = name
        self.likes = likes


class SingerNode:
    def __init__(self, singer, playlist):
        self.singer = singer
        self.playlist = playlist
        self.prev = None
        self.next = None


class SingerList:  # Doubly linked list ADT
    def __init__(self):
        self.head = None
        self.tail = None

        # Add predefined singers, songs, and likes
        self.add_predefined_singers()

    def add_predefined_singers(self):
        s1 = "Taylor Swift"
        songs1 = [
            Song("Bad Blood", 1000),
            Song("Fireworks", 20000),
            Song("Blank Space", 232653)
        ]
        self.insert(s1, songs1)

        s2 = "Shreya Ghoshal"
        songs2 = [
            Song("Teri Meri", 50045),
            Song("Pal", 12465)
        ]
        self.insert(s2, songs2)

        s3 = "Gautham Karthik"
        songs3 = [
            Song("Hoyna", 83898),
            Song("Yenno Yenno", 18802),
            Song("Hawa Hawa", 234575)
        ]
        self.insert(s3, songs3)

    def insert(self, singer, songs):
        playlist = MaxHeap(10)  # object of max heap
        for song in songs:
            playlist.insert(song)

        singer_node = SingerNode(singer, playlist)  # object of doubly linked list node

        if self.head is None:
            self.head = singer_node
            self.tail = singer_node
        else:
            self.tail.next = singer_node
            singer_node.prev = self.tail
            self.tail = singer_node

    def addSinger(self):
        singer = input("Enter singer name: ")
        songs = []
        num = int(input("Enter the number of songs for this singer: "))
        for i in range(num):
            song_name = input("Enter song name: ")
            song_likes = int(input("Enter song likes: "))
            songs.append(Song(song_name, song_likes))
        self.insert(singer, songs)
        print(f"{singer} added successfully!")
        print()

    def deleteSong(self, singer_name, song_name):
        current = self.head
        while current:
            if current.singer == singer_name:
                current.playlist.deleteSong()
                return
            current = current.next

    def mostPlayedSong(self):
        if not self.head:
            print("No singers or songs available.")
            return

        current = self.head

        while current:
            if current.playlist.storage:
                print(f"Most played song for {current.singer}: {current.playlist.storage[0].name}")
            else:
                print(f"No songs available for {current.singer}")
            current = current.next

    def mostLikedSong(self):
        if not self.head:
            print("No singers or songs available.")
            return

        most_liked_song = None
        most_liked_singer = None

        current = self.head
        while current:
            if current.playlist.storage:
                current_song = current.playlist.storage[0]

                if not most_liked_song or current_song.likes > most_liked_song.likes:
                    most_liked_song = current_song
                    most_liked_singer = current.singer

            current = current.next

        if most_liked_song:
            print(f"Most liked song among all singers: {most_liked_song.name} by {most_liked_singer}")
        else:
            print("No songs available.")
        print()


    def deleteSongMenu(self):
        singer = input("Enter singer name: ")
        song_name = input("Enter song name to delete: ")
        self.deleteSong(singer, song_name)
        print(f"{song_name} deleted from {singer}'s playlist.")
        print()

    def searchSong(self, song_name):
        current = self.head
        while current:
            for song in current.playlist.storage:
                if song and song.name == song_name:
                    print(f"Song found: {song.name} by {current.singer}. Likes: {song.likes}")
                    print()
                    return
            current = current.next
        print("Song not found.")
        print()

    def displayAllSingers(self):
        current = self.head

        if current is None:
            print("No singers or songs available.")
            print()
            return

        while current:
            print(f"Singer: {current.singer}")
            if current.playlist.storage:
                print("Songs:")
                for song in current.playlist.storage[:current.playlist.size]:
                    print(f"- {song.name} (Likes: {song.likes})")
            else:
                print("No songs available.")
            print()
            current = current.next

# Testing the implementation

singerList = SingerList()

while True:
    print("*********** Menu ***********")
    print("1. Add Singer")
    print("2. Delete Song")
    print("3. Search Song")
    print("4. Display All Singers and Songs")
    print("5. Most Played Song for Each Singer")
    print("6. Most Liked Song Among All Singers")
    print("7. Exit")
    choice = int(input("Enter your choice: "))

    if choice == 1:
        singerList.addSinger()
    elif choice == 2:
        singerList.deleteSongMenu()
    elif choice == 3:
        song_name = input("Enter song name to search: ")
        singerList.searchSong(song_name)
    elif choice == 4:
        singerList.displayAllSingers()
    elif choice == 5:
        singerList.mostPlayedSong()
    elif choice == 6:
        singerList.mostLikedSong()
    elif choice == 7:
        break
    else:
        print("Invalid choice! Please try again.")
