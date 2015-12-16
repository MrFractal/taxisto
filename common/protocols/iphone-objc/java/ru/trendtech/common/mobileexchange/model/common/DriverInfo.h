#import "NSMutableArray.h"
#import "NSMutableArray.h"

/**
 * Created by max on 06.02.14.
 */

@interface DriverInfo : NSObject {
  long id;
  NSString * firstName;
  NSString * lastName;
  NSString * phone;
  int totalRating;
  NSString * autoModel;
  NSString * autoNumber;
  int autoClass;
  NSString * autoColor;
  float moneyBalance;
  NSString * photoUrl;
  NSString * photoPicture;
  NSMutableArray * photosCarsUrl;
  NSMutableArray * photosCarsPictures;
  NSString * login;
  NSString * password;
}

@property(nonatomic) long id;
@property(nonatomic, retain) NSString * firstName;
@property(nonatomic, retain) NSString * lastName;
@property(nonatomic, retain) NSString * phone;
@property(nonatomic) int totalRating;
@property(nonatomic, retain) NSString * autoModel;
@property(nonatomic, retain) NSString * autoNumber;
@property(nonatomic) int autoClass;
@property(nonatomic, retain) NSString * autoColor;
@property(nonatomic) float moneyBalance;
@property(nonatomic, retain) NSString * photoUrl;
@property(nonatomic, retain) NSMutableArray * photosCarsUrl;
@property(nonatomic, retain) NSString * login;
@property(nonatomic, retain) NSString * password;
@property(nonatomic, retain) NSString * photoPicture;
@property(nonatomic, retain) NSMutableArray * photosCarsPictures;
- (void) init;
- (void) setId:(long)id;
- (void) setFirstName:(NSString *)firstName;
- (void) setLastName:(NSString *)lastName;
- (void) setPhone:(NSString *)phone;
- (void) setTotalRating:(int)totalRating;
- (void) setAutoModel:(NSString *)autoModel;
- (void) setAutoNumber:(NSString *)autoNumber;
- (void) setAutoClass:(int)autoClass;
- (void) setAutoColor:(NSString *)autoColor;
- (void) setMoneyBalance:(float)moneyBalance;
- (void) setPhotoUrl:(NSString *)photoUrl;
- (void) setPhotosCarsUrl:(NSMutableArray *)photosCarsUrl;
- (void) setLogin:(NSString *)login;
- (void) setPassword:(NSString *)password;
- (void) setPhotoPicture:(NSString *)photoPicture;
- (void) setPhotosCarsPictures:(NSMutableArray *)photosCarsPictures;
@end
