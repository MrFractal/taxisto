#import "DriverInfo.h"

@implementation DriverInfo

@synthesize id;
@synthesize firstName;
@synthesize lastName;
@synthesize phone;
@synthesize totalRating;
@synthesize autoModel;
@synthesize autoNumber;
@synthesize autoClass;
@synthesize autoColor;
@synthesize moneyBalance;
@synthesize photoUrl;
@synthesize photosCarsUrl;
@synthesize login;
@synthesize password;
@synthesize photoPicture;
@synthesize photosCarsPictures;

- (void) init {
  if (self = [super init]) {
    photosCarsUrl = [[[NSMutableArray alloc] init] autorelease];
    photosCarsPictures = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [firstName release];
  [lastName release];
  [phone release];
  [autoModel release];
  [autoNumber release];
  [autoColor release];
  [photoUrl release];
  [photoPicture release];
  [photosCarsUrl release];
  [photosCarsPictures release];
  [login release];
  [password release];
  [super dealloc];
}

@end
