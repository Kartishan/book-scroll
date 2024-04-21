import {
    AUTH_ROUTE,
    HOME_ROUTE,
    STORY_ROUTE,
    BOOK_ROUTE,
    ALLBOOKS_ROUTE,
    BOOKSBYNAME_ROUTE,
    BOOKSBYCATEGORY, HISTORY_ROUTE, READ_BOOK_ROUTE
} from "./utils/consts";
import Auth from "./pages/auth/Auth";
import Home from "./pages/home/Home";
import storyScroll from "./pages/storyScroll/StoryScroll";
import bookDetails from "./pages/bookPage/bookDetails";
import books from "./pages/books/books";
import booksByName from "./pages/booksByName/booksByName";
import BooksByCategory from "./pages/books/booksByCategory/booksByCategory";
import history from "./pages/history/history";
import EpubReader from "./components/EpubReader";

export const publicRoutes = [
    {
        path: AUTH_ROUTE,
        Component: Auth
    },
    {
        path:HOME_ROUTE,
        Component:Home
    },
    {
        path: STORY_ROUTE,
        Component: storyScroll
    },
    {
        path: BOOK_ROUTE,
        Component: bookDetails
    },
    {
        path: ALLBOOKS_ROUTE,
        Component: books
    },
    {
        path: BOOKSBYNAME_ROUTE,
        Component: booksByName
    },
    {
        path: BOOKSBYCATEGORY,
        Component: BooksByCategory
    },
    {
        path: HISTORY_ROUTE,
        Component: history
    },
    {
        path: READ_BOOK_ROUTE,
        Component: EpubReader,
    }
]